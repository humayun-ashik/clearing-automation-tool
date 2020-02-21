# Clearing Automation Tool for DFS Project
This is a tool prepared for reducing the cost of time for manual testing in a fintech project.

## [Project overview: Digital Financial Services (DFS)] </br>
This project is about ensuring financial services through mobile platform. Financial services includes: user-to-user send money, cash-out from agent, mobile recharge, E-commerce pay, Bill Pay for the user app. Similarly user app, there are more 3 apps for the agent, dso and distributor. Agent can cash in, merchant pay etc. Distributors are the employee of the system and under their supervision dso operates. There are investors, owners, solution providers who shares the benefit earned from the project. 

## [Clearing concept]
- [x] For some transactions system disburse commission to the selected user either in real time or after a period. Flow of e-money, commission calculation, fee calculation both for the real time and for a certain range of time should be handled very carefully. 
- [x] There are some virtual ledger and sub-ledger account no. to hold the e-money for respective purpose. When it’s time to settle the e-money of the charged, then there a bunch of calculation, batch disbursement etc. are performed. This series of operation regarding the settlement of e-money can be referred simply as ‘Clearing’.

## [Purpose of clearing]
* Check the fee-commission calculation is correct for all the transactions performed with specification.
* Check the debit-credit is performed correctly for correct partners.
* Check the Settlement date is according to the Given settlement period (e.g. Monthly/Weekly/Ad-hoc ).
* Check the player wise vat for every transaction event.
* …

## [List Of Transaction events]
* Send e-money (App/Ussd)
* Cash In (App/ Ussd)
* Cash Out (App/Ussd)
* Merchant Payment (App/Ussd)
* Bill Pay
* Merchant Cash Out(App/ussd).
* Bulk Disbursement (Portal/Web)

## [Data Movement Flow Of Clearing]
- [x] Data inserted into clearing table by extracting the server log. There are two mediums to perform any transaction – app and ussd. Two types of log are being written in server – one for the transactions and another for the ussd operation(telecom operators charged DFS system for using their service). 
- [x] Then file aggregator aggregate the log file and data written into the database table for each type of log.
- [x] Then a batch is being created and after then fee-commission is calculated as per configuration for each type of transactions
- [x] Then separated data are accumulated and finalize the amount for the GL to GL movement with their defined date.

## [Sample Calculation For – Cash-In]

Let’s say agent perform cash in 1000 taka to any customer. For this transaction, let’s see the calculation:</br>
> Service fee = 1000 * 1.5% = 15  					(service charge for transaction)</br>
> Adv. commission = 15 – (15 * 15% vat) = 13.2				(VAT deduction of transaction)</br>
> Now, this adv. Commission’s 40.45% disbursed to Agent and Dh 		(This is decided value)</br>
> Agent commission = 13.2 * 34.9% = 4.6068				(34.9% is the percentage commission for agent)</br>
> AIT of agent commission = 4.6068 * 10% = 0.46068			(AIT = Advance Income Tax)</br>
> Agent Gross Commission = 4.6068 – 0. 46068 = 4.14612</br>
> DH commission = 13.2 * 5.55% = 0.7326				(5.55% is the commission for distributor)</br>
> AIT of DH Commission = 0.7326 * 12% = 0.087912</br>
> DH Gross Commission = 0.7326 – 0.087912 = 0.644688</br>

## [Previous approach for clearing]
This complex and time consuming task of clearing is carried out by manual inspection. 
Approch:
* Do some transactions (CI,CO,P2P,TopUp, BillPay)
* Log in to IAS, USSDGW, CLR user in QA server
* In CLR user go to the /home/ias/clr and copy the latest Clearing log to local space
* In USSDGW user go to /home/ussdgw/clr and copy the latest Clearing log to local space
* In CLR user go to the /home/clr/DFS-CLR/test_clr
* Here you will see two folders
  1. ussd_gw
  1. ias_clr
* Copy the previously IAS clr file to ias_clr folder
* Copy the previously USSDGW clr file to ussd_gw folder
* Now go to http://xxx.xxx.xxx.xxx:xxxx/user/login and login using these credentials
* In the main page you will see two projects. Click on DFS_CLR. Click on the “>” button of the scheduler and you will see a job named start.sh. Click on that
* On the upper right corner you will see a green button named “Run Job Now”, click it
* Below the loading bar there is a tab named Log Output, Click on that you will see that the logs are printing for the running job. Wait for the system to finish running clearing.

## [Automation project structure + description]
Our project followed micro-service architecture. There is a component called “clearing” on which clearing operation is performed.</br>
> &#8594; [Automate Clearing Job Run]  &#8594; [Process Query] </br>
> &#8594;	[Verify Fee-Commission Calculation] 	&#8594; 	[Verify GL to GL Movement] </br>
> &#8594;	[Verify Debit-Credit of E-money]	&#8594; 	[Generate Report]

## [Automate Clearing Job Run]
For automating clearing job running a shell script is written for doing the steps which was performed earlier manually:
> ## prepareClearingFile.sh
This script is actually invoked from the main groovy-gradle project. 

## [Current Project Code Structure]
 

## [How code will be worked]
Project root class is MainRunner.java. It’s functionalities:
* It establish a connection with the oracle database using Oracle’s jdbc driver from connections.DBConnection: port, sid, database username and database password required.
* It calls runClearing() class and inside this class there is a method called executeFile() which takes the filepath(for this project the path is: “/home/testScriptForClearing/prepareClearingFile.sh”) of your shell script. To execute shell script, Java Secure Channel(JSch from JCraft: http://www.jcraft.com/jsch/) is used. To establish connection it requires host name, host ip, port and host password. It then run the script using sh command.
* After running clearing job  it takes the user input regarding the clearing items:
```
==============================================================================
Clearing Items:
==============================================================================
ITEM 1. [Customer --> P2P]		ITEM 2. [Customer --> Cash-In]
ITEM 3. [Customer --> Cash-Out]	ITEM 4. [Customer --> BillPay]
ITEM 5. [Agent --> BillPay]		Other keys. [All items]
==============================================================================
```
* As per user’s input it calls the respective clearing item class and then the rest operation done from these classes.
* Suppose user chose the ITEM 4. [Customer --> BillPay]. Then items.CustomerBillPay class will be called. It holds the db connection info and using this it executes the pre-written sql queries through utils.ExecuteQuery class. Inside this class there is a method, executeSqlQuery() which takes the sql query to leverage and the processed information is stored into list. All the required sql queries are writen into utils.Queries class. 
  * Inside this items.CustomerBillPay class there is a method RunClearing() which is responsible for processing the series of result.
  * At first it finds the transaction sequence no(we call it NR no.) through CommonMethods.getNrQueryResult() method 
  * Then it finds the configured  and calculated fee-commission
* If the fee-commission is static then it finds the fee-commission by calling CommonMethods.getViewFeeComResult() method by providing respective transaction channel(app or ussd), processing code etc. And then the calculated fee-commission is retrieved by calling CommonMethods.getFeeComQueryResult() method.
* If the fee-commission is differed from one to another i.e., dynamic fee-commission e.g., bill-pay then a single method is called - CommonMethods.getAndViewFeeComQueryResult()
  * After then, results of the transactions are accumulated by CommonMethods.getAccumulatedDailyClearingQuery() method.
  * After then, CommonMethods.getAccumulatedSettlementQuery() method will be called and extracts the settled totals transaction amount, total transaction count with GL to GL movement as per fee items.
  * And then CommonMethods.getCumulativeSettlementLogQuery() method is called for showing the settlement status, settlement date, GL movement and the e-money sender player, e-money receiver player.
* After all the operation done, db connection will be closed.

## [Dependencies: Required tools]
- IDE: Intellij
- JDK 8 or higher
- Gradle
- Jdbc jar
- Jsch jar

## [How to run]
Run /test/groovy/main/MainRunner.java 

## [Future Improvements]
- Implement reporting for the extracted result.
- Extend this tool for all possible transaction event.
- Extend tool for lottery project’s clearing if possible.


