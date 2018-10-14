# Intermediary_Account
mvn -e exec:java -Dexec.mainClass="za.co.apricotdb.support.util.ApricotUtils" -Dexec.args="scan driver=com.microsoft.sqlserver.jdbc.SQLServerDriver url=jdbc:sqlserver://POC000472:53199;databaseName=intermediary_account user=Intermediary_Account_Mig password=!n+3rm34!@ry@cc0un+MIG"

# Intermediary_Party
mvn -e exec:java -Dexec.mainClass="za.co.apricotdb.support.util.ApricotUtils" -Dexec.args="scan driver=com.microsoft.sqlserver.jdbc.SQLServerDriver url=jdbc:sqlserver://POC000472:53199;databaseName=intermediary_party user=Intermediary_Party_Mig password=!n+3rm34!@ry9@r+yMIG"

# Intermediary_Agreement
mvn -e exec:java -Dexec.mainClass="za.co.apricotdb.support.util.ApricotUtils" -Dexec.args="scan driver=com.microsoft.sqlserver.jdbc.SQLServerDriver url=jdbc:sqlserver://POC000472:53199;databaseName=intermediary_agreement user=Intermediary_Agreement_Mig password=!n+3rm34!@ry@9r33m3n+MIG"

# Intermediary_Compensation
mvn -e exec:java -Dexec.mainClass="za.co.apricotdb.support.util.ApricotUtils" -Dexec.args="scan driver=com.microsoft.sqlserver.jdbc.SQLServerDriver url=jdbc:sqlserver://POC000472:53199;databaseName=intermediary_compensation user=Intermediary_Compensation_Mig password=!n+3rm34!@ryc0m93n$@710nMIG"


#ACC_DATABASE=intermediary_account
#ACC_HOST=POC000472
#ACC_PORT=53199
#ACC_INSTANCE=MIG
#ACC_USER=Intermediary_Account_Mig
#ACC_PASSWORD=!n+3rm34!@ry@cc0un+MIG

#STAGING_DATABASE=intermediary_party
#STAGING_HOST=POC000472
#STAGING_PORT=53199
#STAGING_INSTANCE=MIG
#STAGING_USER=Intermediary_Party_Mig
#STAGING_PASSWORD=!n+3rm34!@ry9@r+yMIG

# Intermediary Agreement POC_MIG
#AGREEMENT_DATABASE=intermediary_agreement
#AGREEMENT_HOST=POC000472
#AGREEMENT_PORT=53199
#AGREEMENT_INSTANCE=MIG
#AGREEMENT_USER=Intermediary_Agreement_Mig
#AGREEMENT_PASSWORD=!n+3rm34!@ry@9r33m3n+MIG

#COMP_DATABASE=intermediary_compensation
#COMP_HOST=POC000472
#COMP_PORT=53199
#COMP_INSTANCE=MIG
#COMP_USER=Intermediary_Compensation_Mig
#COMP_PASSWORD=!n+3rm34!@ryc0m93n$@710nMIG
