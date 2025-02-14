# Repair the failed transaction using Spring AI 

The tech used behind is ollama and deepseek-r1 

collect data to make the llm learn using the below settings
like 

**Model file**

```
FROM deepseek-r1
PARAMETER temperature 0.7
PARAMETER top_p 0.9
PARAMETER repetition_penalty 1.1
FINETUNE txn.jsonl
```

**Sample data**

|Transaction ID	|ISO 20022 Message	|Failure Reason	|Corrective Action|
|----|---|----|---|
|TXN12345|	<pacs.002><Reason>AC06</Reason></pacs.002>	|Account Closed	|Notify customer and request an alternate account|
TXN67890	|<pacs.002><Reason>AM04</Reason></pacs.002>	|Insufficient Funds	|Retry after 24 hours or request a top-up|
TXN11111	|<pacs.002><Reason>RC01</Reason></pacs.002>	|Invalid Bank Identifier	|Validate IBAN/SWIFT and retry|
TXN22222	|<pacs.002><Reason>BE09</Reason></pacs.002>	|Invalid Mandate	|Request a new mandate from the customer|
TXN33333	|<pacs.002><Reason>TM01</Reason></pacs.002>	|Transaction Timeout|	Retry the transaction with a priority flag|

**failed_txns.jsonl**
```
{"messages": [
    {"role": "system", "content": "You are an AI assistant analyzing failed ISO 20022 payment transactions and suggesting corrective actions."},
    {"role": "user", "content": "Transaction ID: TXN12345\nISO 20022 Message:\n<pacs.002><Reason>AC06</Reason></pacs.002>"},
    {"role": "assistant", "content": "Reason: Account Closed. Action: Notify customer and request an alternate account."}
]}
{"messages": [
    {"role": "system", "content": "You are an AI assistant analyzing failed ISO 20022 payment transactions and suggesting corrective actions."},
    {"role": "user", "content": "Transaction ID: TXN67890\nISO 20022 Message:\n<pacs.002><Reason>AM04</Reason></pacs.002>"},
    {"role": "assistant", "content": "Reason: Insufficient Funds. Action: Retry after 24 hours or request a top-up."}
]}
{"messages": [
    {"role": "system", "content": "You are an AI assistant analyzing failed ISO 20022 payment transactions and suggesting corrective actions."},
    {"role": "user", "content": "Transaction ID: TXN11111\nISO 20022 Message:\n<pacs.002><Reason>RC01</Reason></pacs.002>"},
    {"role": "assistant", "content": "Reason: Invalid Bank Identifier. Action: Validate IBAN/SWIFT and retry."}
]}
```
**ollama**
```
ollama create txn-repair-model -f failed_txns.jsonl
```

Store the actions and results in vector DB , here it is postgress pgvector


**Sample Prompt**
```
You are an AI assistant analyzing failed ISO 20022 payment transactions and suggesting corrective actions.

Consider the following factors:
1. Customer details  is in the xpath {xpath_customer}
2. Additional details  is in the xpath {xpath_add_detail}
3. Amount Transacted  is in the xpath {xpath_amount}
4. Time Taken to Address  is in the xpath {xpth_time}
5. Failed reason is {failed reason}a

---

### Failed Transaction details :
TxnId: {txnid}
content: {content}
type: {type}
Timestamp: {timestamp}

### Similar Past Failed Transaction details :
{similar_failed_txn_details}

Based on the above, and what corrective action should be taken?
1) self repair **please respond with following json**
{
	"action":"call the below URL to update the data",
	"url":"http://xys.com/correctit",
}
or
1) notify the respective customer **please respond with following json**
{
	"action":"call the below number and inform",
	"phone":"00000000",
	"reason":failed reason, ask customer to correct teh missing data
}

```



