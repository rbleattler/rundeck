package rundeck

import grails.test.hibernate.HibernateSpec
import testhelper.RundeckHibernateSpec

class ReferencedExecutionSpec extends RundeckHibernateSpec
{
    List<Class> getDomainClasses() { [ScheduledExecution, Workflow, CommandExec, ReferencedExecution]}
    def "execution id list"(){
        given:
        def refTotal = 10
        def se = new ScheduledExecution(
                uuid: "000000",
                jobName: 'test1',
                project: 'project1',
                groupPath: 'testgroup',
                doNodedispatch: true,
                filter:'name: ${option.nodes}',
                refExecCount: refTotal,
                workflow: new Workflow(
                        keepgoing: true,
                        commands: [
                                new CommandExec([
                                        adhocRemoteString: 'test buddy',
                                        argString: '-delay 12 -monkey cheese -particle'
                                ])
                        ]
                )
        ).save()
        def seb = new ScheduledExecution(
                uuid: UUID.randomUUID().toString(),
                jobName: 'test2',
                project: 'project1',
                groupPath: 'testgroup',
                doNodedispatch: true,
                filter:'name: ${option.nodes}',
                refExecCount: refTotal,
                workflow: new Workflow(
                        keepgoing: true,
                        commands: [
                                new CommandExec([
                                        adhocRemoteString: 'test buddy',
                                        argString: '-delay 12 -monkey cheese -particle'
                                ])
                        ]
                )
        ).save()
        def exec = new Execution(
                user: "testuser",
                project: "project1",
                loglevel: 'WARN',
                status: 'FAILED',
                doNodedispatch: true,
                filter:'name: nodea',
                succeededNodeList:'fwnode',
                failedNodeList: 'nodec xyz,nodea',
                workflow: new Workflow(commands: [new CommandExec(adhocExecution: true, adhocRemoteString: 'a remote string')]).save(),
                jobUuid: seb.uuid
        ).save()

        def re = new ReferencedExecution(jobUuid: se.uuid,execution: exec).save()

        when:
        List l = ReferencedExecution.executionProjectList(se.uuid)

        then:
        l.size() == 1
        l == ["project1"]
    }

    def "execution id list with max result"(){
        given:
        def refTotal = 10
        def se = new ScheduledExecution(
                uuid: "000000",
                jobName: 'test1',
                project: 'project1',
                groupPath: 'testgroup',
                doNodedispatch: true,
                filter:'name: ${option.nodes}',
                refExecCount: refTotal,
                workflow: new Workflow(
                        keepgoing: true,
                        commands: [
                                new CommandExec([
                                        adhocRemoteString: 'test buddy',
                                        argString: '-delay 12 -monkey cheese -particle'
                                ])
                        ]
                )
        ).save()
        def seb = new ScheduledExecution(
                uuid: UUID.randomUUID().toString(),
                jobName: 'test2',
                project: 'project1',
                groupPath: 'testgroup',
                doNodedispatch: true,
                filter:'name: ${option.nodes}',
                refExecCount: refTotal,
                workflow: new Workflow(
                        keepgoing: true,
                        commands: [
                                new CommandExec([
                                        adhocRemoteString: 'test buddy',
                                        argString: '-delay 12 -monkey cheese -particle'
                                ])
                        ]
                )
        ).save()
        def exec = new Execution(
                user: "testuser",
                project: "project1",
                loglevel: 'WARN',
                status: 'FAILED',
                doNodedispatch: true,
                filter:'name: nodea',
                succeededNodeList:'fwnode',
                failedNodeList: 'nodec xyz,nodea',
                workflow: new Workflow(commands: [new CommandExec(adhocExecution: true, adhocRemoteString: 'a remote string')]).save(),
                jobUuid: seb.uuid
        ).save()

        def exec2 = new Execution(
                user: "testuser",
                project: "project1",
                loglevel: 'WARN',
                status: 'FAILED',
                doNodedispatch: true,
                filter:'name: nodea',
                succeededNodeList:'fwnode',
                failedNodeList: 'nodec xyz,nodea',
                workflow: new Workflow(commands: [new CommandExec(adhocExecution: true, adhocRemoteString: 'a remote string')]).save(),
                jobUuid: seb.uuid
        ).save()

        def re = new ReferencedExecution(jobUuid: se.uuid,execution: exec).save()
        def re2 = new ReferencedExecution(jobUuid: se.uuid,execution: exec2).save()

        def executionIdList = [[executionId: exec.id, project: exec.project], [executionId: exec2.id, project: exec2.project]]

        when:
        List l = ReferencedExecution.executionProjectList(se.uuid, max)

        then:
        l.size() == sizeList
        l == ["project1"]

        where:
        max  | sizeList
        0    | 1
        1    | 1
        2    | 1
    }

    def "parent list"(){
        given:
        def refTotal = 10
        def se = new ScheduledExecution(
                uuid: "000000",
                jobName: 'test1',
                project: 'project1',
                groupPath: 'testgroup',
                doNodedispatch: true,
                filter:'name: ${option.nodes}',
                refExecCount: refTotal,
                workflow: new Workflow(
                        keepgoing: true,
                        commands: [
                                new CommandExec([
                                        adhocRemoteString: 'test buddy',
                                        argString: '-delay 12 -monkey cheese -particle'
                                ])
                        ]
                )
        ).save()
        def seb = new ScheduledExecution(
                uuid: UUID.randomUUID().toString(),
                jobName: 'test2',
                project: 'project1',
                groupPath: 'testgroup',
                doNodedispatch: true,
                filter:'name: ${option.nodes}',
                refExecCount: refTotal,
                workflow: new Workflow(
                        keepgoing: true,
                        commands: [
                                new CommandExec([
                                        adhocRemoteString: 'test buddy',
                                        argString: '-delay 12 -monkey cheese -particle'
                                ])
                        ]
                )
        ).save()
        def exec = new Execution(
                user: "testuser",
                project: "project1",
                loglevel: 'WARN',
                status: 'FAILED',
                doNodedispatch: true,
                filter:'name: nodea',
                succeededNodeList:'fwnode',
                failedNodeList: 'nodec xyz,nodea',
                workflow: new Workflow(commands: [new CommandExec(adhocExecution: true, adhocRemoteString: 'a remote string')]).save(),
                jobUuid: seb.uuid
        ).save()

        def re = new ReferencedExecution(jobUuid: se.uuid,execution: exec).save()

        when:
        List l = ReferencedExecution.parentListScheduledExecutionUuid(se.uuid, 0)

        then:
        l.size() == 1
        l[0] == seb.uuid
    }

    def "parent list with max result"(){
        given:
        def refTotal = 10
        def se = new ScheduledExecution(
                uuid: "000000",
                jobName: 'test1',
                project: 'project1',
                groupPath: 'testgroup',
                doNodedispatch: true,
                filter:'name: ${option.nodes}',
                refExecCount: refTotal,
                workflow: new Workflow(
                        keepgoing: true,
                        commands: [
                                new CommandExec([
                                        adhocRemoteString: 'test buddy',
                                        argString: '-delay 12 -monkey cheese -particle'
                                ])
                        ]
                )
        ).save()
        def seb = new ScheduledExecution(
                uuid: "11111",
                jobName: 'test2',
                project: 'project1',
                groupPath: 'testgroup',
                doNodedispatch: true,
                filter:'name: ${option.nodes}',
                refExecCount: refTotal,
                workflow: new Workflow(
                        keepgoing: true,
                        commands: [
                                new CommandExec([
                                        adhocRemoteString: 'test buddy',
                                        argString: '-delay 12 -monkey cheese -particle'
                                ])
                        ]
                )
        ).save()

        def seb2 = new ScheduledExecution(
                uuid: "22222",
                jobName: 'test3',
                project: 'project1',
                groupPath: 'testgroup',
                doNodedispatch: true,
                filter:'name: ${option.nodes}',
                refExecCount: refTotal,
                workflow: new Workflow(
                        keepgoing: true,
                        commands: [
                                new CommandExec([
                                        adhocRemoteString: 'test buddy',
                                        argString: '-delay 12 -monkey cheese -particle'
                                ])
                        ]
                )
        ).save()
        def exec = new Execution(
                user: "testuser",
                project: "project1",
                loglevel: 'WARN',
                status: 'FAILED',
                doNodedispatch: true,
                filter:'name: nodea',
                succeededNodeList:'fwnode',
                failedNodeList: 'nodec xyz,nodea',
                workflow: new Workflow(commands: [new CommandExec(adhocExecution: true, adhocRemoteString: 'a remote string')]).save(),
                jobUuid: seb.uuid
        ).save()

        def exec2 = new Execution(
                user: "testuser",
                project: "project1",
                loglevel: 'WARN',
                status: 'FAILED',
                doNodedispatch: true,
                filter:'name: nodea',
                succeededNodeList:'fwnode',
                failedNodeList: 'nodec xyz,nodea',
                workflow: new Workflow(commands: [new CommandExec(adhocExecution: true, adhocRemoteString: 'a remote string')]).save(),
                jobUuid: seb2.uuid
        ).save()

        def re = new ReferencedExecution(jobUuid: se.uuid,execution: exec).save()
        def re2 = new ReferencedExecution(jobUuid: se.uuid,execution: exec2).save()

        when:
        List l = ReferencedExecution.parentListScheduledExecutionUuid(se.uuid, max)

        then:
        l.size() == sizeList
        l*.toString() == result

        where:
        max  | sizeList | result
        0    | 2        | ["11111", "22222"]
        1    | 1        | ["11111"]
        2    | 2        | ["11111", "22222"]
    }
}
