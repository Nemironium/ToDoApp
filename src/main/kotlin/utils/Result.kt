package utils

data class Result<out T>(val status: Status, val data: T?, val message: String?) {

    val isSuccessful: Boolean
        get() =
            status == Status.SUCCESS

    val isFailed: Boolean
        get() = !isSuccessful

    companion object {
        fun <T> success(data: T): Result<T> {
            return Result(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): Result<T> {
            return Result(Status.ERROR, data, message)
        }
    }
}