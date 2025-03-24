package com.example.tapplication.ui

import com.example.tapplication.management.*
import com.example.tapplication.library.*
import com.example.tapplication.shop.*
import java.time.Month

class LibraryApp {
    private val library = LibraryManager()
    private val purchaseManager = PurchaseManager()
    private val digitizationOffice = DigitalizationService()

    private val libraryItems = listOf<LibraryItem>(
        Book(101, true, "Мастер и Маргарита", 500, "М. Булгаков"),
        Book(102, true, "Преступление и наказание", 672, "Ф. Достоевский"),
        Newspaper(201, true, "Коммерсант", 789, Month.MARCH),
        Newspaper(202, true, "Известия", 1023, Month.FEBRUARY),
        Disk(301, true, "Интерстеллар", "DVD"),
        Disk(302, true, "Пинк Флойд - The Wall", "CD"),
    )

    init {
        library.addListItems(libraryItems)
    }

    fun start() {
        while(true) {
            println("\nВыберите категорию:")
            println("1. Показать книги")
            println("2. Показать газеты")
            println("3. Показать диски")
            println("4. Управление менеджером")
            println("5. Выход")

            when(readlnOrNull()?.toIntOrNull()) {
                1 -> showItems<Book>()
                2 -> showItems<Newspaper>()
                3 -> showItems<Disk>()
                4 -> manageStore()
                5 -> {
                    println("Выход из программы.")
                    return
                }
                else -> println("Ошибка: Введите число от 1 до 4.")
            }
        }
    }

    private inline fun<reified T: LibraryItem> showItems() {
        val items = library.getItemsByType<T>()
        if (items.isEmpty()) {
            println("Нет доступных объектов.")
            return
        }

        println("\nСписок объектов:")
        items.forEachIndexed { index, item -> println("${index + 1}. ${item.getShortInfo()}") }
        println("${items.size + 1}. Назад")

        when (val choice = readlnOrNull()?.toIntOrNull()) {
            in 1..items.size -> choice?.let {choice -> showItemActions(items[choice-1].id) }
            items.size + 1 -> return
            else -> println("Ошибка: Введите корректный номер.")
        }
    }

    private fun showItemActions(id: Int){
        val item = library.getItemById(id)
        if (item == null) {
            println("Ошибка: Объект не найден.")
            return
        }

        while(true) {
            println("\nВыберите действие с объектом '${item.name}':")
            println("1. Взять домой")
            println("2. Читать в читальном зале")
            println("3. Показать подробную информацию")
            println("4. Вернуть")
            if (item is Book || item is Newspaper) println("5. Оцифровать")
            println("6. Назад")

            when(readlnOrNull()?.toIntOrNull()) {
                1 -> println(item.takeHome())
                2 -> println(item.readInLibrary())
                3 -> println(item.getDetailedInfo())
                4 -> println(item.returnItem())
                5 -> {
                    if (item is Digitalizable) {
                        val disk = digitizationOffice.digitize(item)
                        println("Оцифровка завершена: ${disk.getDetailedInfo()}")
                        library.addItems(disk)
                    } else {
                        println("Ошибка: Этот объект нельзя оцифровать.")
                    }
                }
                6 -> return
                else -> println("Ошибка: Введите число от 1 до 5.")
            }
        }
    }

    private fun manageStore() {
        while (true) {
            println("\nВыберите магазин:")
            println("1. Купить книгу")
            println("2. Купить газету")
            println("3. Купить диск")
            println("4. Назад")

            when (readlnOrNull()?.toIntOrNull()) {
                1 -> {
                    val book = purchaseManager.buy(BookShop())
                    println("Покупка завершена: ${book.getDetailedInfo()}")
                    library.addItems(book)
                }
                2 -> {
                    val newspaper = purchaseManager.buy(NewspaperKiosk())
                    println("Покупка завершена: ${newspaper.getDetailedInfo()}")
                    library.addItems(newspaper)
                }
                3 -> {
                    val disk = purchaseManager.buy(DiskShop())
                    println("Покупка завершена: ${disk.getDetailedInfo()}")
                    library.addItems(disk)
                }
                4 -> return
                else -> println("Ошибка: Введите число от 1 до 4.")
            }
        }
    }
}