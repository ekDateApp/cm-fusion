import com.codeborne.selenide.WebDriverRunner;
import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class LoginPage {
    private final static String BASE_URL = "https://staging.cmfusion.com/";

    @Test
    @DisplayName("New User Sign Up")
    @Owner("Eugene Kurilenko")
    @Feature("Sign Up")

    void newUserRegistration(){
        step("'Account Sign Up' Page opening", () -> {
            open(BASE_URL);
            $(".login").click();
            $(withText("Sign up")).click();
        });

        Faker faker= new Faker();
        String firstName = faker.name().firstName(),
                lastName = faker.name().lastName(),
                email = "qa+cmf"+"-"+firstName+lastName+"@fmpdev.net",
                phone = faker.phoneNumber().cellPhone(),
                company = faker.company().name(),
                password = "12345678",
                conuntry = "United Kingdom",
                timezone = "(GMT+00:00) London";


        step("Filling the form and Click to create", () -> {
            $("#user_first_name").setValue(firstName);
            $("#user_last_name").setValue(lastName);
            $("#user_email").setValue(email);
            $("#user_contact_number").setValue(phone);
            $("#user_account_attributes_company_attributes_name").setValue(company);
            $("#user_password").setValue(password);
            $("#user_account_attributes_default_country").click();
            $(byText(conuntry)).scrollTo().click();
            $(byText(timezone)).scrollTo().click();
            $(byValue("Create Free Account")).scrollTo().click();
        });

        step("Checking if a new user is created", () -> {
            $(".alert-success").shouldHave(text("Account created successfully"));
            WebDriverRunner.url().equals(BASE_URL+"dashboard?new_account=true");
            $(".cm-header .nav-user").shouldHave(text(firstName));

        });

        step("Checking if user's data is saved correclty", () -> {
            $(".cm-header .nav-user").click();
            $(withText("Profile Information")).click();
            $("#user_first_name").shouldHave(value(firstName));
            $("#user_last_name").shouldHave(value(lastName));
            $("#user_contact_number").shouldHave(value(phone));
            $("#user_account_attributes_default_country").$("option").shouldHave(attribute("selected")).shouldHave(text(conuntry));
            $(".table-edged td").shouldHave(text(email));
            $("#user_account_attributes_company_attributes_name").shouldHave(value(company));

        });
    }

}
