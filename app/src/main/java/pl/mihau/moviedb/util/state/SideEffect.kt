package pl.mihau.moviedb.util.state

interface SideEffect {

    companion object {

        fun of(block: () -> Unit) = object : SideEffect {
            override fun execute() = block()
        }

        val empty = object : SideEffect {
            override fun execute() {
            }
        }
    }

    fun execute()
}