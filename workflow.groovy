def execute() {
    stage 'Build - clean install'
    sh 'mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install'
    step $class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'

    stage 'Update sonar stats'
    sh 'mvn sonar:sonar'
}

return this;
