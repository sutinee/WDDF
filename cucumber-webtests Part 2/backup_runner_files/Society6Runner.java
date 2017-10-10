package net.thucydides.showcase.cucumber;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
		features= { "src/test/resources/features/society6store/" } )
public class Society6Runner {}
