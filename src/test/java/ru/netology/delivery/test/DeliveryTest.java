package ru.netology.delivery.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.Keys.BACK_SPACE;

public class DeliveryTest {
    String Message1 = "Встреча успешно запланирована на ";
    String Message2 = "У вас уже запланирована встреча на другую дату. Перепланировать?";

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

        @Test
        void shouldSuccessfulPlanAndReplanMeeting() {
            var validUser = DataGenerator.Registration.generateUser("ru");
            var daysToAddForFirstMeeting = 4;
            var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
            var daysToAddForSecondMeeting = 7;
            var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
            $("[data-test-id='city'] input").setValue(validUser.getCity());
            $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), BACK_SPACE);
            $("[data-test-id='date'] input").setValue(firstMeetingDate);
            $("[data-test-id='name'] input").setValue(validUser.getName());
            $("[data-test-id='phone'] input").setValue(validUser.getPhone());
            $("[data-test-id='agreement']").click();
            $$("button").find(exactText("Запланировать")).click();
            $("[data-test-id = 'success-notification'] .notification__content").shouldHave(exactText(Message1 + firstMeetingDate)).shouldBe(visible, Duration.ofSeconds(15));
            $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), BACK_SPACE);
            $("[data-test-id='date'] input").setValue(secondMeetingDate);
            $("button.button").click();
            $("[data-test-id='replan-notification'] .notification__content").shouldHave(text(Message2)).shouldBe(visible);
            $("[data-test-id='replan-notification'] button").click();
            $("[data-test-id = 'success-notification'] .notification__content").shouldHave(exactText(Message1 + secondMeetingDate)).shouldBe(visible);

        }
    }


