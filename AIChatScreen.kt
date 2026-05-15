@Composable
fun AIChatScreen() {
    Column {
        Text("AI Chat 🤖")

        var message by remember { mutableStateOf("") }

        TextField(value = message, onValueChange = { message = it })

        Button(onClick = { /* Gemini API call */ }) {
            Text("Send")
        }
    }
}
