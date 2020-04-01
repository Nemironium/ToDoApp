package console

interface ConsoleContract {
    interface ListMenu {
        fun getListMenuValue(isShowSelectList: Boolean): Int?

        fun getNewListName(): String?

        fun getSelectedListName(availableLists: List<String>): String?

        fun printSelectedList(listName: String)

        fun showError(message: String?)

        fun showDeleteConfirmation(listName: String): Boolean?
    }

    interface TaskMenu {
        fun getTaskMenuValue(isShowSelectTask: Boolean?): Int?
    }

}