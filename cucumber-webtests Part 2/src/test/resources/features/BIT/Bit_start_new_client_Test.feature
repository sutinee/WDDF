@BIT
Feature: Start a new Client

Background:
Given User is logged on
	
@Test
Scenario: User starts a new client by enter required fields 
When User clicks tab Resource Paths
 	And User fills Form Definition in section AFP tab Resource Paths
	|Name					|Path																	|
	|Client				|%BASEPATH%/vip/user/%CLIENT%/deflib	|			
	|Tax					|%BASEPATH%/vip/user/tax/deflib				|
	|KCR					|%BASEPATH%/vip/user/kcr/deflib				|
	
