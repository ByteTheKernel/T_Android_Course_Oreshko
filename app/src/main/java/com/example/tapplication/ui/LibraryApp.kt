package com.example.tapplication.ui

import com.example.tapplication.manager.LibraryManager
import com.example.tapplication.library.*

class LibraryApp {
    private val library = LibraryManager()

    init {
        library.addItem(Book(101, true, "Мастер и Маргарита", 500, "М. Булгаков"))
        library.addItem(Book(102, true, "Преступление и наказание", 672, "Ф. Достоевский"))
        library.addItem(Newspaper(201, true, "Коммерсант", 789))
        library.addItem(Newspaper(202, true, "Известия", 1023))
        library.addItem(Disk(301, true, "Интерстеллар", "DVD"))
        library.addItem(Disk(302, true, "Пинк Флойд - The Wall", "CD"))
    }

    fun start() {
        while(true) {
            println("\nВыберите категорию:")
            println("1. Показать книги")
            println("2. Показать газеты")
            println("3. Показать диски")
            println("4. Выход")

            when(readlnOrNull()?.toIntOrNull()) {
                1 -> showItems(Book::class.java)
                2 -> showItems(Newspaper::class.java)
                3 -> showItems(Disk::class.java)
                4 -> {
                    println("Выход из программы.")
                    return
                }
                else -> println("Ошибка: Введите число от 1 до 4.")
            }
        }
    }

    private fun showItems(type: Class<out LibraryItem>) {
        val items = library.getItemsByType(type)
        if (items.isEmpty()) {
            println("Нет доступных объектов.")
            return
        }

        println("\nСписок объектов:")
        items.forEachIndexed { index, item -> println("${index + 1}. ${item.getShortInfo()}") }
        println("${items.size + 1}. Назад")

        when (val choice = readlnOrNull()?.toIntOrNull()) {
            in 1..items.size -> choice?.let { showItemActions(type, it) }
            items.size + 1 -> return
            else -> println("Ошибка: Введите корректный номер.")
        }
    }

    private fun showItemActions(type: Class<out LibraryItem>, index: Int){
        val item = library.getItemByIndex(type, index)
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
            println("5. Назад")

            when(readlnOrNull()?.toIntOrNull()) {
                1 -> println(library.borrowItem(item, home = true))
                2 -> println(library.borrowItem(item, home = false))
                3 -> println(item.getDetailedInfo())
                4 -> println(library.returnItem(item))
                5 -> return
                else -> println("Ошибка: Введите число от 1 до 5.")
            }
        }
    }
}