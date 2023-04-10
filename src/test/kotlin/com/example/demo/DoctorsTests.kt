package com.example.demo

import com.codeborne.selenide.Condition
import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.Selenide
import org.junit.jupiter.api.Test
import kotlin.random.Random

class DoctorsTests {
    @Test
    fun navLinksAreWorking() {
        Selenide.open("http://localhost:4200/")
        Selenide.`$`(".home").shouldHave(Condition.text("Home"))

        val doctorsLink = Selenide.`$`(".doctors");
        val teamsLink = Selenide.`$`(".teams");

        doctorsLink.shouldHave(Condition.text("Doctors"))
        teamsLink.shouldHave(Condition.text("Medical teams"))

        doctorsLink.click()
        Selenide.`$`("#doctors").should(Condition.exist)

        teamsLink.click()
        Selenide.`$`("#teams").should(Condition.exist)
    }

    @Test
    fun formIsWorking() {
        Selenide.open("http://localhost:4200/doctors")

        Selenide.`$`("#form").should(Condition.exist)

        val firstName = "Al"
        val lastName = "Boliit"

        createDoctor(firstName, lastName)

        val lastRowTexts = getTableRows().last().findAll("td").texts()
        lastRowTexts[1].contains(firstName)
        lastRowTexts[2].contains(lastName)

        orderTableById(true)

        val row = getTableRows().first()
        val firstRowTexts = row.findAll("td").texts()
        firstRowTexts[1].contains(firstName)
        firstRowTexts[2].contains(lastName)

        val count = getTableRows().count()

        // delete row
        row.find("button.mdi-delete").click()

        // assert
		assert(getTableRows().count() == count - 1)
    }

    @Test
    fun searchIsWorking() {
        Selenide.open("http://localhost:4200/doctors")

        val random = Random.nextInt(1000, 9999)

        val firstName = "Test".plus(random)
        val lastName = "Search".plus(random)

        createDoctor(firstName, lastName)

        setInputValue("#search", firstName)

        var firstRowTexts = getTableRows().first().findAll("td").texts()
        firstRowTexts[1].contains(firstName)
        firstRowTexts[2].contains(lastName)

        setInputValue("#search", lastName)

        firstRowTexts = getTableRows().first().findAll("td").texts()
        firstRowTexts[1].contains(firstName)
        firstRowTexts[2].contains(lastName)

        // cleanup should be actually done before every test :)
        setInputValue("#search")
    }

    @Test
    fun deleteIsWorking() {
        Selenide.open("http://localhost:4200")
        Selenide.open("http://localhost:4200/doctors")

        val random = Random.nextInt(1000, 9999)

        val firstName2 = "It Will".plus(random)
        val lastName2 = "Remain".plus(random)
        createDoctor(firstName2, lastName2)

        val firstName1 = "Will Be".plus(random)
        val lastName1 = "Deleted".plus(random)
        createDoctor(firstName1, lastName1)

        orderTableById(true)

        val firstTableRow = getTableRows().first()
        // delete row
        firstTableRow.find("button.mdi-delete").click()

        val firstRowTexts = firstTableRow.findAll("td").texts()
        firstRowTexts[1].contains(firstName2)
        firstRowTexts[2].contains(lastName2)
    }

    private fun orderTableById(desc: Boolean = false) {
        // sorting
        val ths = Selenide.`$$`("#doctors .doctors-table thead th.sortable")
        val idHeader = ths.first()
        val anyOtherHeader = ths.last()

        // order by last asc
        anyOtherHeader.click()

        // order by asc
        idHeader.click()

        if (desc) {
            // order by desc
            idHeader.click()
        }
    }

    private fun createDoctor(firstName: String, lastName: String) {
        setInputValue("#first-name", firstName)
        setInputValue("#last-name", lastName)

        Selenide.`$`(".v-date-picker-title__year").click()
        Selenide.`$`(".v-date-picker-years li").click()
        Selenide.`$`(".v-date-picker-table.v-date-picker-table--month button").click()
        Selenide.`$`(".v-date-picker-table.v-date-picker-table--date button").click()

        Selenide.`$`("#submit").should(Condition.enabled).click()
    }

    private fun setInputValue(selector: String, value: String = "") {
        val el = Selenide.`$`(selector)

        // el.clear()
        // el.setValue(value)
        // for some reason commented out code above does not work :(

        for (i in 1..value.length) {
            el.sendKeys("\b")
        }
        if (value.isNotEmpty()) {
            el.sendKeys(value)
        }
    }

    private fun getTableRows(): ElementsCollection {
        return Selenide.`$$`("#doctors .doctors-table tbody tr")
    }
}
