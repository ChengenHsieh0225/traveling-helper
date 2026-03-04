import httpx
from datetime import datetime
from dateutil.relativedelta import relativedelta

async def get_rate_history(from_ccy: str, to_ccy: str, timespan: str):
    # return 1
    valid_span = ['1w', '2m', '6m', '1y', '2y']

    if timespan not in valid_span:
        return []
    
    today = datetime.now()
    latest_date = today - relativedelta(days=1)
    
    dates = {}
    dates["1w"] = [(latest_date - relativedelta(days=i)).strftime("%Y-%m-%d") for i in range(6, 0, -1)]
    dates["2m"] = [(latest_date - relativedelta(weeks=i)).strftime("%Y-%m-%d") for i in range(7, 0, -1)]
    dates["6m"] = [(latest_date - relativedelta(weeks=2*i)).strftime("%Y-%m-%d") for i in range(11, 0, -1)]
    dates["1y"] = [(latest_date - relativedelta(months=i)).strftime("%Y-%m-%d") for i in range(11, 0, -1)]
    dates["2y"] = [(latest_date - relativedelta(months=2*i)).strftime("%Y-%m-%d") for i in range(11, 0, -1)]

    date_to_rate_list = []

    for date in dates[timespan]:
        url = f"https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@{date}/v1/currencies/{from_ccy}.json"
        async with httpx.AsyncClient() as client:
            response = await client.get(
                url,
                timeout=20.0
            )
            data = response.json()
            rate = data.get(from_ccy).get(to_ccy)
            date_to_rate_list.append({
                'time': date,
                'rate': rate
            })

    return date_to_rate_list