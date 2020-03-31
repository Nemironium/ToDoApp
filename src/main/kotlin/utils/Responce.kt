package utils

data class Responce<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR
    }

    companion object {
        fun <T> success(data: T): Responce<T> {
            return Responce(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): Responce<T> {
            return Responce(Status.ERROR, data, message)
        }
    }
}