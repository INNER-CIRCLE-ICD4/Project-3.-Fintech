package com.sendy.domain.account

object AccountNumberValidator {
    private const val ACCOUNT_NUMBER_SIZE = 13

    /**
     * 계좌번호가 올바른 형식인지 검증
     * 형식: 321-xxxx-xx-xxxx (13자리 숫자)
     *
     * @param accountNumber 검증할 계좌번호
     * @return 검증 결과
     */
    fun isValidFormat(accountNumber: String): Boolean {
        // 1. 13자리인지 확인
        if (accountNumber.length != ACCOUNT_NUMBER_SIZE) {
            return false
        }

        // 2. 숫자로만 구성되어 있는지 확인
        if (!accountNumber.all { it.isDigit() }) {
            return false
        }

        // 3. 321로 시작하는지 확인
        if (!accountNumber.startsWith("321")) {
            return false
        }

        return true
    }

    /**
     * 계좌번호 형식 검증 및 예외 발생
     *
     * @param accountNumber 검증할 계좌번호
     * @throws IllegalArgumentException 형식이 올바르지 않은 경우
     */
    fun validateFormat(accountNumber: String) {
        require(accountNumber.length == ACCOUNT_NUMBER_SIZE) { "계좌번호는 13자리여야 합니다." }
        require(accountNumber.all { it.isDigit() }) { "계좌번호는 숫자만 가능합니다." }
        require(accountNumber.startsWith("321")) { "계좌번호는 321로 시작해야 합니다." }
    }

    /**
     * 계좌번호를 하이픈이 포함된 형식으로 변환
     * 예: 3211234567890 -> 321-1234-56-7890
     *
     * @param accountNumber 원본 계좌번호
     * @return 하이픈이 포함된 계좌번호
     */
    fun formatWithHyphens(accountNumber: String): String {
        validateFormat(accountNumber)
        return "${
            accountNumber.substring(
                0,
                3,
            )
        }-${accountNumber.substring(3, 7)}-${accountNumber.substring(7, 9)}-${accountNumber.substring(9, 13)}"
    }

    /**
     * 하이픈이 포함된 계좌번호에서 하이픈 제거
     * 예: 321-1234-56-7890 -> 3211234567890
     *
     * @param formattedAccountNumber 하이픈이 포함된 계좌번호
     * @return 하이픈이 제거된 계좌번호
     */
    fun removeHyphens(formattedAccountNumber: String): String = formattedAccountNumber.replace("-", "")
}
