package com.example.demo

import com.codeborne.selenide.Condition
import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import org.junit.jupiter.api.Test
import org.openqa.selenium.Keys

class MedicalTeamsTests {
	@Test
	fun editIsWorking() {
		Selenide.open("http://localhost:4200/teams")

		Selenide.`$`("#teams").should(Condition.exist)

		// edit first team
		getTableRows().first().find("button.mdi-pencil").click()

		// check first and last checkboxes
		if (!getFirstCheckbox().`is`(Condition.checked)) {
			setCheckbox(getFirstCheckbox())
		}

		if (!getLastCheckbox().`is`(Condition.checked)) {
			setCheckbox(getLastCheckbox())
		}

		// back
		Selenide.`$`("button").click()

		// edit first team again
		getTableRows().first().find("button.mdi-pencil").click()

		// should be checked
		getFirstCheckbox().shouldBe(Condition.checked)
		getLastCheckbox().shouldBe(Condition.checked)

		// back
		Selenide.`$`("button").click()

		// edit last team
		getTableRows().last().find("button.mdi-pencil").click()

		// should not be checked
		getFirstCheckbox().shouldNotBe(Condition.checked)
		getLastCheckbox().shouldNotBe(Condition.checked)

		setCheckbox(getFirstCheckbox())
		setCheckbox(getLastCheckbox())

		// back
		Selenide.`$`("button").click()

		// ensure first team unchecked
		getTableRows().first().find("button.mdi-pencil").click()

		getFirstCheckbox().shouldNotBe(Condition.checked)
		getLastCheckbox().shouldNotBe(Condition.checked)

		// back
		Selenide.`$`("button").click()

		// edit last team again
		val lastTeamId = getTableRows().last().findAll("td").first().text()
		getTableRows().last().find("button.mdi-pencil").click()

		getFirstCheckbox().shouldBe(Condition.checked)
		getLastCheckbox().shouldBe(Condition.checked)

		// ensure doctors table updated
		Selenide.open("http://localhost:4200/doctors")
		val rows = Selenide.`$$`("#doctors .doctors-table tbody tr")

		rows.first().findAll("td")[4].shouldBe(Condition.matchText(lastTeamId))
		rows.last().findAll("td")[4].shouldBe(Condition.matchText(lastTeamId))
	}

	private fun getFirstCheckbox(): SelenideElement {
		return Selenide.`$$`("input[type=checkbox]").first()
	}

	private fun getLastCheckbox(): SelenideElement {
		return Selenide.`$$`("input[type=checkbox]").last()
	}

	private fun setCheckbox(el: SelenideElement) {
		el.sendKeys(Keys.SPACE)
	}

	private fun getTableRows(): ElementsCollection {
		return Selenide.`$$`("#teams table tbody tr")
	}
}
