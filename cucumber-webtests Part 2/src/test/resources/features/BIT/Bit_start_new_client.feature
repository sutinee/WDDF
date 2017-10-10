@BIT
Feature: Start a new Client

Background:
Given User is logged on
	
@Start_a_new_client_by_enter_required_fields
Scenario: User starts a new client by enter required fields 
Given User starts a new Client Configuration
When User enters Client Directory Name "testauto"
When User clicks tab Client
	And User fills general info in tab Client
	|National Resources Client Directory Name	|Client Full Name	|
	|tau																			|Test Auto				|
	And User fills CSR Web Configuration info in tab Client
	|CSR Username	|Client Logo Filename	|Customer Service Text Type	|Main Search Date Range (months)|Batch Retention	|Role-based Access Control (RBAC)	|Master Report	|Admin Reports	|Test Script	|Corp Numbers	|
	|test					|testauto-logo.jpg		|Entire Client Contact			|1															|ticked						|ticked														|not-ticked			|ticked					|not-ticked		|not-ticked		|
	And User fills Default Client NDM Configuration info in tab Client
	|NDM Type	|FILENAME	|File Type	|
	|OTSKC		|BSP.*		|AFP				|
When User clicks tab Database
	And User fills Configuration info in tab Database
	|Database Name	|
	|Q41130					|
	And User fills Additional Options in tab Database
	|Keep Existing Security Setting	|
	|ticked													|
	And User validates Database setup in tab Database 
	#Always Clean Database setup
When User clicks tab Resource Paths
 	And User fills Form Definition in section AFP tab Resource Paths
	|Name					|Path																	|
	|Client				|%BASEPATH%/vip/user/%CLIENT%/deflib	|			
	|Tax					|%BASEPATH%/vip/user/tax/deflib				|
	|KCR					|%BASEPATH%/vip/user/kcr/deflib				|
When User clicks tab Doc Types
	#Add Doc Statement
	And User add new Document Definition
	|File Format	|DocumentType-Pattern	|Subdocument Type	|
	|AFP					|statement-Statement	|shareholderstmt	|
	# How-to: DocumentType-Pattern options (case-sensitive):statement-Statement, confirm-Confirm, trac-Trac, compliance-Compliance, statement-Statement, tax-Tax
	And User fill in tab file of new document type
	|Description						|recdelim	|formdef								|Data Filename																																			|Field Number (Sub1)	|Key Match (Sub1)|Description (Sub1)		|Reconciliation	|Reconcile Processor		|Reconcile Processor Abort|Reconcile Fail Action							|
	|Shareholder Statement	|AFPSF		|Client - F1BSPIN0.OBJ	|/home/loader/processing/cevp/BSP.OTX.BSP.INVM.0000002.1.000360183.AFP_D01.T060001	|4						 				|INVM						 |Investor Statements		|ticked					|EBCDIC_CNTLFILE(*.cntl)|True - Both MisMatch			|Load_Accept - Issue Trouble Ticket	|	
	#Add Doc Statement
	#And User add new Document Definition
	#|File Format	|DocumentType-Pattern	|Subdocument Type	|
	#|AFP					|tax-Tax							|Tax							|
	
