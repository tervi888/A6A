package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.LoginPage;
import ru.netology.page.DashboardPage;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MoneyTransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }
    @Test
    void shouldTransferFromFirstToSecond(){
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount),firstCardInfo);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(getFirstCardInfo());
        var actualBalanceSecondCard = dashboardPage.getCardBalance(getSecondCardInfo());
        assertEquals(expectedBalanceFirstCard,actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard,actualBalanceSecondCard);
    }
    @Test
    void shouldGetErrorMassageIfAmountMoreBalance(){
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generateInvalidAmount(firstCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        transferPage.makeTransfer(String.valueOf(amount),secondCardInfo);
        transferPage.findErrorMassage
                ("Выполнена попытка перевода суммы, превышающей остаток на карте списания");
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals(firstCardBalance,actualBalanceFirstCard);
        assertEquals(secondCardBalance,actualBalanceSecondCard);
    }
}