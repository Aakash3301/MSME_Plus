package com.msme.plus.shared.data.mock

object DashboardMockData {
    val DASHBOARD_JSON = """
        {
         "statusCode": 200,
          "message": "DashBoard successful",
        "data":{
          "companyName": "ABC Manufacturing",
          "sector": "Manufacturing Sector",
          "hasNotifications": true,
          "profileImageUrl": "https://lh3.googleusercontent.com/aida-public/AB6AXuCMWFXvsH8kOw-IWGcffc9SK9FFI7TCyKcXm5PJG4PE5RsahKulmMNfnPVNo9Z7MN3WWAPXTYIl86I-rbAvn7mvMTo6CKSKNDtplykrEBKHdW2ffs-a6zykGEDs7dt2smnihTgJ8KGgQD7fQHb0gqJ5sCJQfLCC0qunTQg4X6UYNFVyK6BWDx9yv6ui271THAF-8vRL_iZiT71gnfeaxOBn8VDvK8klAwQRF0pjdTP7kjpi7MiamwFR",
          "healthScore": {
            "score": 87,
            "maxScore": 100,
            "statusText": "Healthy Business",
            "tags": ["LOW RISK", "LOAN READY"]
          },
          "kpis": [
            {
              "id": "revenue",
              "icon": "payments",
              "title": "Monthly Revenue",
              "value": "₹18.5L",
              "trend": "+12%",
              "trendDirection": "up",
              "colorType": "primary"
            },
            {
              "id": "cash_flow",
              "icon": "account_balance_wallet",
              "title": "Cash Flow",
              "value": "Excellent",
              "trend": "Stable Profile",
              "trendDirection": "neutral",
              "colorType": "tertiary"
            },
            {
              "id": "gst_compliance",
              "icon": "gavel",
              "title": "GST Compliance",
              "value": "98%",
              "trend": "On-time Filing",
              "trendDirection": "neutral",
              "colorType": "secondary"
            },
            {
              "id": "upi_growth",
              "icon": "rocket_launch",
              "title": "UPI Growth",
              "value": "+18%",
              "trend": "Market expansion",
              "trendDirection": "up",
              "colorType": "primary"
            }
          ],
          "loanEligibility": {
            "status": "INSTANT ELIGIBILITY",
            "amountText": "₹25 Lakh",
            "confidencePercentage": 92
          },
          "quickActions": [
            {
              "id": "health_card",
              "icon": "health_metrics",
              "title": "Financial\nHealth Card"
            },
            {
              "id": "revenue_analytics",
              "icon": "analytics",
              "title": "Revenue\nAnalytics"
            },
            {
              "id": "alternate_data",
              "icon": "hub",
              "title": "Data\nSources"
            },
            {
              "id": "ai_advisor",
              "icon": "smart_toy",
              "title": "AI\nAdvisor"
            },
            {
              "id": "loan_assessment",
              "icon": "account_balance",
              "title": "Loan\nAssessment"
            },
            {
              "id": "business_documents",
              "icon": "description",
              "title": "Business\nDocuments"
            }
          ]
        }
        }
    """.trimIndent()
}
