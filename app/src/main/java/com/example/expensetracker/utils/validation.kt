package com.example.expensetracker.utils

data class ValidationRule (
    val message: String,
    val check: (String) -> Boolean
)

data class ValidationField(
    val value: String,
    val rules: List<ValidationRule>,
    val setError: (String?) -> Unit
)

fun validate(value: String, rules: List<ValidationRule>): String? {
    return  rules.firstOrNull {rule -> rule.check(value) }?.message
}

fun validateAllFields(fields: List<ValidationField>): Boolean {
    fields.forEach { (value, rules, setError) ->
        val error = validate(value, rules)
        setError(error)
    }
    return fields.all { (value, rules, _) -> validate(value, rules) == null }
}

val titleRules = listOf(
    ValidationRule("Title should not be empty") { it.isEmpty() },
    ValidationRule("Title should be less than 50 characters") { it.length > 50 }
)

val amountRules = listOf(
    ValidationRule("Amount should not be empty") { it.isEmpty() },
    ValidationRule("Enter a valid number") { it.toDoubleOrNull() == null },
    ValidationRule("Amount should be greater than 0") { (it.toDoubleOrNull() ?: 0.0) <= 0 },
    ValidationRule("Amount should be less than 100 billion") { (it.toDoubleOrNull() ?: 0.0) >= 100_000_000_000.0 }
)

val budgetRules = listOf(
    ValidationRule("Budget should not be empty") { it.isEmpty() },
    ValidationRule("Enter a valid number") { it.toDoubleOrNull() == null },
)

val alertRules = listOf(
    ValidationRule("Alert threshold should not be empty") { it.isEmpty() },
    ValidationRule("Enter a valid number") { it.toIntOrNull() == null },
    ValidationRule("Alert threshold should be between 0 and 100") { (it.toIntOrNull() ?: 0) !in 0..100 }
)