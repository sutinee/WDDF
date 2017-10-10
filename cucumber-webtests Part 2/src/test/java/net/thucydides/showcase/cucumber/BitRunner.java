package net.thucydides.showcase.cucumber;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
		features = "src/test/resources/features/BIT/"
		//,tags={"@Start_a_new_client_by_import"}
		//,tags={"@Start_a_new_client_by_enter_required_fields"}
		,tags={"@Test"}
		)
public class BitRunner {}
