<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Lock</title>
</head>
<body>
<h1>Create Lock</h1>
<form action="lock" method="post">
    <input type="hidden" name="action" value="create">
    <label for="account_num">Account Number:</label>
    <input type="text" id="account_num" name="account_num" required><br>
    <label for="locked_amount">Locked Amount:</label>
    <input type="number" id="locked_amount" name="locked_amount" required><br>
    <label for="lock_start_date">Lock Start Date:</label>
    <input type="datetime-local" id="lock_start_date" name="lock_start_date" required><br>
    <label for="lock_end_date">Lock End Date:</label>
    <input type="datetime-local" id="lock_end_date" name="lock_end_date" required><br>
    <label for="lock_type">Lock Type:</label>
    <select id="lock_type" name="lock_type" required>
        <option value="WITHDRAWAL">Withdrawal</option>
        <option value="LOAN">Loan</option>
    </select><br>
    <label for="interest_rate">Interest Rate:</label>
    <input type="number" step="0.01" id="interest_rate" name="interest_rate" required><br>
    <button type="submit">Create Lock</button>
</form>
</body>
</html>
