package br.eng.ecarrara.vilibra.usecase

import android.support.test.runner.AndroidJUnit4
import br.eng.ecarrara.vilibra.BookListActivity
import br.eng.ecarrara.vilibra.robot.bookDetails
import br.eng.ecarrara.vilibra.robot.contacts
import br.eng.ecarrara.vilibra.robot.lentBooks
import br.eng.ecarrara.vilibra.robot.searchBook
import br.eng.ecarrara.vilibra.rule.ActivityWithAllDataClearedTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LendBook {

    @get:Rule
    var testInitializationRule = ActivityWithAllDataClearedTestRule(BookListActivity::class.java)

    @Test
    fun lendBookToContact() {
        val expectedIsbn = "8575224123"
        val expectedBookTitle = "Dominando o Android"

        lentBooks {
            addBookLending()
        }
        searchBook {
            fillIsbn(expectedIsbn)
            confirm()
        }
        bookDetails {
            checkForBookTitle(expectedBookTitle)
            checkForBookIsbn10("ISBN-10: $expectedIsbn")
            lend()
        }
        contacts {
            pickContact("Meu Irmao")
        }
        lentBooks {
            checkForBookTitle(expectedBookTitle)
        }
    }

}