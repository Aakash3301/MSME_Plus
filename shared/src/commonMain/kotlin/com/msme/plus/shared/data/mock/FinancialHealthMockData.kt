package com.msme.plus.shared.data.mock

object FinancialHealthMockData {
    val FINANCIAL_HEALTH_JSON = """
        {
            "statusCode": 200,
            "message": "Success",
            "data": {
                "companyName": "IDBI MSME",
                "profileImageUrl": "https://lh3.googleusercontent.com/aida-public/AB6AXuC4zP2ruezlC-xYoJn6RZopIz3N7IB3QLROZ_5nNMXv2qdmKkcviUJoTTb1B3yDHK98RDY5gk66s5qz4MGAFMnP8DIX_yjtSS6EIU8wCFfpwdQLkoprA0LM76DeNaxRGslglJjcTSitK06OIlrUirnHqvIMYVKqr5wqRky4F5dq_0wf457bkqnNkLbWYDJpF6PsRAd4Ci5oU7VzL7jO6WkAyYs_acPoTdOW3XBQ_9FcFogWOLNiq7nr",
                "overallScore": 87,
                "maxScore": 100,
                "statusText": "Your business shows high fiscal discipline and robust digital adoption. You are in the top 10% of MSMEs in your sector.",
                "badges": [
                    {
                        "text": "Platinum Grade",
                        "icon": "verified",
                        "type": "PRIMARY"
                    },
                    {
                        "text": "Improving",
                        "icon": "trending_up",
                        "type": "TERTIARY"
                    }
                ],
                "scoreBreakdowns": [
                    {
                        "label": "GST Compliance",
                        "value": 95,
                        "color": "PRIMARY"
                    },
                    {
                        "label": "Revenue Stability",
                        "value": 92,
                        "color": "PRIMARY"
                    },
                    {
                        "label": "Digital Transactions",
                        "value": 91,
                        "color": "PRIMARY"
                    },
                    {
                        "label": "Employee Stability",
                        "value": 90,
                        "color": "PRIMARY"
                    },
                    {
                        "label": "Cash Flow",
                        "value": 88,
                        "color": "PRIMARY"
                    },
                    {
                        "label": "Vendor Payment",
                        "value": 86,
                        "color": "PRIMARY"
                    },
                    {
                        "label": "Business Growth",
                        "value": 83,
                        "color": "PRIMARY"
                    }
                ],
                "strengths": [
                    "GST filed consistently",
                    "Revenue growing",
                    "Healthy UPI collections"
                ],
                "risks": [
                    "Cash reserves slightly low",
                    "Vendor payment cycle increasing"
                ],
                "loanOffer": {
                    "title": "Unlock Business Loan Up to ₹50L",
                    "description": "Based on your excellent Health Score, you are pre-approved for immediate disbursement at competitive rates.",
                    "buttonText": "Apply Now"
                }
            }
        }
    """.trimIndent()
}
