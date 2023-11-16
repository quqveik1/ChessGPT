package com.kurlic.chessgpt.online

import com.tinder.scarlet.Stream
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow

data class Action(var name: String)
data class Event(var name: String)

@Deprecated("CRINGE")
interface MyWebSocketService {
    @Receive
    fun observeWebSocketEvent(): Stream<WebSocket.Event>

    @Send
    fun sendAction(action: Action)

    @Receive
    fun observeEvents(): Stream<Event>
}