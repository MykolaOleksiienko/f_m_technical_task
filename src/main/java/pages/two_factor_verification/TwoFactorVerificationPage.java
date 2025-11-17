package pages.two_factor_verification;

import annotations.common.URI;
import assertions.CustomAssertions;
import io.qameta.allure.Step;
import pages.AbstractBaseSteps;

import static locator_templates.LocatorTemplate.getLocatorByDataAttribute;

@URI(url = "/two-factor-verification")
public class TwoFactorVerificationPage extends AbstractBaseSteps {
    private static final String twoFactorVerificationTitle = getLocatorByDataAttribute("two-factor-verification-title");

    @Step("Verify Two Factor Verification Page is present")
    public void verifyTwoFactorVerificationPagePresent() {
        waitPageContentLoad();
        CustomAssertions.assertThatElementIsVisible(findElement(twoFactorVerificationTitle), true, "Two Factor Verification Page");
    }
}
