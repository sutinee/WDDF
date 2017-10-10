package se.thinkcode.itake;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/resources/se/thinkcode/itake/login"
		,glue={"src/test/java/se/thinkcode/itake"}
		)

public class TestRunner {

}
