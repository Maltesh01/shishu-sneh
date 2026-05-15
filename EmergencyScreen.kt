@Composable
fun EmergencyScreen() {
    Column {
        Text("Emergency SOS 🚨")
        Button(onClick = { /* Call API */ }) {
            Text("Call Ambulance")
        }
    }
}
