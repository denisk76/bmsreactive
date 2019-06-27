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
        const val REQUEST = """
            [
           { "cardNum":"1234", "terminalCode":"10", "bill":{"sum":120} },
           { "cardNum":"1234", "terminalCode":"20", "bill":{"sum":120} }
           ]
        """
        const val RESPONSE = """
            [
           { "amount":22, "earn":12, "spend":0 },
           { "amount":34, "earn":24, "spend":0 }
           ]
        """
    }
}