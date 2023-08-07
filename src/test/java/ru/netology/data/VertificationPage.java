package ru.netology.data;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.page.DashboardPage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VertificationPage {
    private final SelenideElement codeField = $("[data-test-id=code] input");
    private final SelenideElement verifyButton = $("[data-test-id=active-verify]");
    public VertificationPage() {
        codeField.shouldBe(visible);
    }
    public DashboardPage validVerify(DataHelper.VertificationCode vertificationCode) {
        codeField.setValue(vertificationCode.getCode());
        verifyButton.click();
        return new DashboardPage();
    }
}
