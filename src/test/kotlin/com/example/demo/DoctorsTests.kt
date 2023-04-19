package com.example.demo

import com.codeborne.selenide.Condition
import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import org.junit.jupiter.api.Test
import kotlin.random.Random

class DoctorsTests {
    @Test
    fun navLinksAreWorking() {
        Selenide.open("http://localhost:4200/")
        findOne(".home").shouldHave(Condition.text("Home"))

        val doctorsLink = findOne(".doctors");
        val teamsLink = findOne(".teams");

        doctorsLink.shouldHave(Condition.text("Doctors"))
        teamsLink.shouldHave(Condition.text("Medical teams"))

        doctorsLink.click()
        findOne("#doctors").should(Condition.exist)

        teamsLink.click()
        findOne("#teams").should(Condition.exist)
    }

    @Test
    fun formIsWorking() {
        Selenide.open("http://localhost:4200/doctors")

        findOne("#form").should(Condition.exist)

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
        row.find(".mdi-delete").click()

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
        firstTableRow.find(".mdi-delete").click()

        val firstRowTexts = firstTableRow.findAll("td").texts()
        firstRowTexts[1].contains(firstName2)
        firstRowTexts[2].contains(lastName2)
    }

    private fun orderTableById(desc: Boolean = false) {
        // sorting
        val ths = findAll("#doctors .doctors-table thead .v-data-table__th--sortable")
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

        findAll(".dp__month_year_select")[1].scrollIntoView(true).click()
        findOne(".dp__overlay .dp__overlay_row .dp__overlay_cell").click()
        findAll(".dp__month_year_select")[0].click()
        findOne(".dp__overlay .dp__overlay_row").findAll(".dp__overlay_col")[1].click()
        findOne(".dp__calendar .dp__calendar_row").findAll(".dp__calendar_item")[6].click()

        findOne("#submit").should(Condition.enabled).click()
    }

    private fun setInputValue(selector: String, value: String = "") {
        val el = findOne(selector)

        el.clear()
        el.value = value
    }

    private fun getTableRows(): ElementsCollection {
        return findAll("#doctors .doctors-table tbody tr")
    }

    private fun findOne(selector: String): SelenideElement {
        return Selenide.`$`(selector)
    }

    private fun findAll(selector: String): ElementsCollection {
        return Selenide.`$$`(selector)
    }
}
