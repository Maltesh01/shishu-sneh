@Composable
fun DashboardScreen(navController: NavController) {
    Column {
        Text("Shishu-Sneh Dashboard")

        Button(onClick = { navController.navigate("feeding") }) {
            Text("Feeding")
        }

        Button(onClick = { navController.navigate("vaccination") }) {
            Text("Vaccination")
        }

        Button(onClick = { navController.navigate("chat") }) {
            Text("AI Chat")
        }
    }
}
