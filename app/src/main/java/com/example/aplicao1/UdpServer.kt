import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlinx.coroutines.*

class UdpServer(private val port: Int) {
    private var socket: DatagramSocket? = null
    private var running = false

    fun start() {
        running = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                socket = DatagramSocket(port)
                val buffer = ByteArray(1024)
                println("UDP server listening on port $port...")

                while (running) {
                    val incomingPacket = DatagramPacket(buffer, buffer.size)
                    socket?.receive(incomingPacket)

                    val data = String(incomingPacket.data, 0, incomingPacket.length)
                    println("Received message: $data")

                    // Process the received data and send a response if needed
                    handleIncomingData(data, incomingPacket.address, incomingPacket.port)
                }
            } catch (e: Exception) {
                println("Error in UDP server: ${e.message}")
            } finally {
                socket?.close()
                println("UDP server stopped.")
            }
        }
    }

    fun stop() {
        running = false
        socket?.close()
    }

    private fun handleIncomingData(data: String, address: InetAddress, port: Int) {
        // Process the received data and send a response if needed
        val response = "Echo: $data"
        val responsePacket = DatagramPacket(
            response.toByteArray(),
            response.toByteArray().size,
            address, port
        )
        socket?.send(responsePacket)
    }
}