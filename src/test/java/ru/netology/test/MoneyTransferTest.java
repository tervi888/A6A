package ru.netology.test;

import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.LoginPage;
import ru.netology.page.DashboardPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataHelper.*;

public class MoneyTransferTest {
    DashboardPage dashboardPage;
    DataHelper dataHelper;
    @BeforeEach
    var loginPage = open("http:/localhost:9999", LoginPage.class);
    var authInfo = getAuthInfo();
    var vertificationPage = loginPage.validLogin(authInfo);
    var vertificationCode = getVertificationCode();
    dashboardPage = vertificationPage.validVerify(vertificationCode);

    @Test
    var firstCardInfo = getFirstCardInfo();
    var secondCardInfo = getSecondCardInfo();
    var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
    var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
    var amount = generateValidAmount(firstCardBalance);
    var expectedBalanceFirstCard = firstCardBalance - amount;
    var expectedBalanceSecondCard = secondCardBalance + amount;
    var transferPage = dashboardPage.selectCardIsTransfer(secondCardInfo);
    dashboardPage = transferPage.makeValidTransfer(String.valueof(amount), firstCardInfo);
    var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
    var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);

    assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);

    assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);

}
