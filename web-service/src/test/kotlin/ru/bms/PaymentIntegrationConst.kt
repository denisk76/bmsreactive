package ru.bms

class PaymentIntegrationConst {
    companion object {
        const val CLUB_DATA = """
            [
            { "terminalCode":"10", "percent":10 },
            { "terminalCode":"20", "percent":20 }
            ]
            """
        const val CLIENTS_DATA = """
           [
           { "cardNum":"1234", "amount":10},
           { "cardNum":"1235", "amount":20}
           ]
        """
    }
}