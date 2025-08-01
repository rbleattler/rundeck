const messages = {
  Edit: "Edit",
  Save: "Save",
  Delete: "Delete",
  Cancel: "Cancel",
  Revert: "Revert",
  jobAverageDurationPlaceholder: "leave blank for Job Average duration",
  resourcesEditor: {
    "Dispatch to Nodes": "Dispatch to Nodes",
    Nodes: "Nodes",
  },
  uiv: {
    modal: {
      cancel: "Cancel",
      ok: "OK",
    },
  },
  cron: {
    section: {
      0: "Seconds",
      1: "Minutes",
      2: "Hours",
      3: "Day of Month",
      4: "Month",
      5: "Day of Week",
      6: "Year",
    },
  },
  message_communityNews: "Community News",
  message_connectionError:
    "It appears an error occured when connecting to Community News.",
  message_readMore: "Read More",
  message_refresh: "Please refresh the page or visit us at",
  message_subscribe: "Subscribe",
  message_delete: "Delete this field",
  message_duplicated: "Field already exists",
  message_select: "Select a Field",
  message_description: "Description",
  message_fieldLabel: "Field Label",
  message_fieldKey: "Field Key",
  message_fieldFilter: "Type to filter a field",
  message_empty: "Can be empty",
  message_cancel: "Cancel",
  message_add: "Add",
  message_addField: "Add Custom Field",
  message_pageUsersSummary: "List of Rundeck users.",
  message_pageUsersLoginLabel: "Username",
  message_pageUsersCreatedLabel: "Created",
  message_pageUsersUpdatedLabel: "Updated",
  message_pageUsersLastjobLabel: "Last Job Execution",
  message_domainUserFirstNameLabel: "First Name",
  message_domainUserLastNameLabel: "Last Name",
  message_domainUserEmailLabel: "Email",
  message_domainUserLabel: "User",
  message_pageUsersTokensLabel: "N\u00BA Tokens",
  message_pageUsersTokensHelp:
    "You can administrate the tokens in the User Profile page.",
  message_pageUsersLoggedStatus: "Status",
  message_pageUserLoggedOnly: "Logged In Users Only",
  message_pageUserNotSet: "Not Set",
  message_pageUserNone: "None",
  message_pageFilterLogin: "Login",
  message_pageFilterHostName: "Host Name",
  message_pageFilterSessionID: "Session ID",
  message_pageFilterBtnSearch: "Search",
  message_pageUsersSessionIDLabel: "Session ID",
  message_pageUsersHostNameLabel: "Host Name",
  message_pageUsersLastLoginInTimeLabel: "Last Login",
  message_pageUsersTotalFounds: "Total Users Found",
  message_paramIncludeExecTitle: "Show Last Execution",
  message_loginStatus: {
    "LOGGED IN": "Logged In",
    "NOT LOGGED": "Never",
    ABANDONED: "Expired",
    "LOGGED OUT": "Logged Out",
  },
  message_userSummary: {
    desc: "This is a list of User Profiles which have logged in to Rundeck.",
  },
  message_webhookPageTitle: "Webhooks",
  message_webhookListTitle: "Webhooks",
  message_webhookDetailTitle: "Webhook Detail",
  message_webhookListNameHdr: "Name",
  message_addWebhookBtn: "Add",
  message_webhookEnabledLabel: "Enabled",
  message_webhookPluginCfgTitle: "Plugin Configuration",
  message_webhookSaveBtn: "Save",
  message_webhookCreateBtn: "Create Webhook",
  message_webhookDeleteBtn: "Delete",
  message_webhookPostUrlLabel: "Post URL",
  message_webhookPostUrlHelp:
    "When a HTTP POST request to this URL is received, the Webhook Plugin chosen below will receive the data.",
  message_webhookPostUrlPlaceholder:
    "URL will be generated after the Webhook is created",
  message_webhookNameLabel: "Name",
  message_webhookUserLabel: "User",
  message_webhookUserHelp:
    "The authorization username assumed when running this webhook. All ACL policies matching this username will apply.",
  message_webhookRolesLabel: "Roles",
  message_webhookRolesHelp:
    "The authorization roles assumed when running this webhook (comma separated). All ACL policies matching these roles will apply.",
  message_webhookAuthLabel: "HTTP Authorization String",
  message_webhookGenerateSecurityLabel: "Use Authorization Header",
  message_webhookGenerateSecretCheckboxHelp:
    "[Optional] A Webhook authorization string can be generated to increase security of this webhook. All posts will need to include the generated string in the Authorization header.",
  message_webhookSecretMessageHelp:
    "Copy this authorization string now. After you navigate away from this webhook you will no longer be able to see the string.",
  message_webhookRegenClicked:
    "A new authorization string will be generated and displayed when the webhook is saved.",
  message_webhookPluginLabel: "Choose Webhook Plugin",
  message_hello: "hello world",
  message_sidebarNotificationText: "Rundeck update available",
  message_updateAvailable: "Update Available",
  message_updateHasBeenReleased: "An update to Rundeck has been released.",
  message_installedVersion: "The installed version of Rundeck is",
  message_currentVersion: "The most recent release of Rundeck is",
  message_getUpdate: "Get Update",
  message_dismissMessage:
    "To dismiss this notification until the next release, please click here.",
  message_close: "Close",
  "bulk.edit": "Bulk Edit",
  "in.of": "in",
  execution: "Execution | Executions",
  "execution.count": "1 Execution | {0} Executions",
  "Bulk Delete Executions: Results": "Bulk Delete Executions: Results",
  "Requesting bulk delete, please wait.":
    "Requesting bulk delete, please wait.",
  "bulkresult.attempted.text": "{0} Executions were attempted.",
  "bulkresult.success.text": "{0} Executions were successfully deleted.",
  "bulkresult.failed.text": "{0} Executions could not be deleted:",
  "delete.confirm.text": "Really delete {0} {1}?",
  "clearselected.confirm.text":
    "Clear all {0} selected items, or only items shown on this page?",
  "bulk.selected.count": "{0} selected",
  "results.empty.text": "No results for the query",
  "Only shown executions": "Only shown executions",
  "Clear bulk selection": "Clear Bulk Selection",
  "Click to edit Search Query": "Click to edit Search Query",
  "Auto refresh": "Auto refresh",
  "error.message.0": "An Error Occurred: {0}",
  "info.completed.0": "Completed: {0}",
  "info.completed.0.1": "Completed: {0} {1}",
  "info.missed.0.1": "Marked Missed: {0} {1}",
  "info.started.0": "Started: {0}",
  "info.started.expected.0.1": "Started: {0}, Estimated Finish: {1}",
  "info.scheduled.0": "Scheduled; starting {0}",
  "job.execution.starting.0": "Starting {0}",
  "job.execution.queued": "Queued",
  "info.newexecutions.since.0":
    "1 New Result. Click to load. | {0} New Results. Click to load.",
  "In the last Day": "In the last Day",
  Referenced: "Referenced",
  "job.has.been.deleted.0": "(Job {0} has been deleted)",
  Filters: "Filters",
  "filter.delete.named.text": 'Delete Filter "{0}"...',
  "Delete Saved Filter": "Delete Saved Filter",
  "filter.delete.confirm.text":
    'Are you sure you want to delete the Saved Filter named "{0}"?',
  "filter.save.name.prompt": "Name:",
  "filter.save.validation.name.blank": "Name Cannot be blank",
  "filter.save.button": "Save Filter...",
  "saved.filters": "Filtros salvos",
  failed: "failed",
  ok: "ok",
  "0.total": "{0} total",

  period: {
    label: {
      All: "any time",
      Hour: "in the last Hour",
      Day: "in the last Day",
      Week: "in the last Week",
      Month: "in the last Month",
    },
  },
  "empty.message.default": "None configured. Click {0} to add a new plugin.",
  CreateAcl: "Create ACL",
  CreateAclName: "ACL Description",
  CreateAclTitle: "Create Key Storage ACL for the project",
  "Edit Nodes": "Edit Nodes",
  Modify: "Modify",
  "Edit Node Sources": "Edit Node Sources",
  "The Node Source had an error": "The Node Source had an error",
  "Validation errors": "Validation errors",

  "unauthorized.status.help.1":
    'Some Node Source returned an "Unauthorized" message.',
  "unauthorized.status.help.2":
    "The Node Source plugin might need access to the Key Storage Resource. it could be enabled by Access Control Policy entries.",
  "unauthorized.status.help.3":
    'Please be sure that the ACL policies enable "read" access to the Key Storage in this project for the project URN path (urn:project:name). ',
  "unauthorized.status.help.4": "Go to {0} to create a Project ACL ",
  "unauthorized.status.help.5": "Go to {0} to create a System ACL ",

  "acl.config.link.title": "Project Settings > Access Control",
  "acl.config.system.link.title": "System Settings > Access Control",
  "acl.example.summary": "Example ACL Policy",

  "page.keyStorage.description":
    "Key Storage provides a global directory-like structure to save Public and Private Keys and Passwords, for use with Node Execution authentication.",

  Duplicate: "Duplicate",
  "Node.count.vue": "Node | Nodes",
  "bulk.delete": "Exclus\u00e3o em massa",
  "select.none": "Selecionar nenhum",
  "select.all": "Selecionar todos",
  "cancel.bulk.delete": "Cancelar a exclus\u00e3o em massa",
  "delete.selected.executions": "Excluir execu\u00e7\u00f5es selecionadas",
  "click.to.refresh": "clique para atualizar",
  "count.nodes.matched": "{0} {1} Correspondido",
  "count.nodes.shown": "{0} n\u00f3s mostrados.",
  "delete.this.filter.confirm": "Really delete this filter?",
  "enter.a.node.filter":
    "Insira um filtro de n\u00f3 ou .* Para todos os n\u00f3s",
  "execute.locally": "Executar localmente",
  "execution.page.show.tab.Nodes.title": "Nodes",
  "execution.show.mode.Log.title": "Sa\u00edda de Log",
  filter: "Filtro:",
  "loading.matched.nodes": "Carregando n\u00f3s correspondentes ...",
  "loading.text": "Carregando...",
  "loglevel.debug": "Debug",
  "loglevel.normal": "Normal",
  "matched.nodes.prompt": "N\u00f3s correspondentes",
  no: "N\u00e3o",
  "node.access.not-runnable.message":
    "Voc\u00ea n\u00e3o tem acesso para executar comandos neste n\u00f3.",
  "node.filter": "Filtro de N\u00f3",
  "node.filter.exclude": "Exclude Filter",
  "node.metadata.os": "Sistema operacional",
  "node.metadata.status": "Status",
  nodes: "N\u00f3s",
  "notification.event.onfailure": "Na falha",
  "notification.event.onsuccess": "No sucesso",
  "notification.event.onstart": "No come\u00e7o",
  "notification.event.onavgduration": "Dura\u00e7\u00e3o m\u00e9dia excedida",
  "notification.event.onretryablefailure": "Na falha de nova tentativa",
  refresh: "atualizar",
  "save.filter.ellipsis": "Salvar filtro",
  "search.ellipsis": "Search\u2026",
  "ScheduledExecution.page.edit.title": "Editar job",
  "ScheduledExecution.page.create.title": "Criar novo job",
  "scheduledExecution.property.defaultTab.label": "Aba padr\u00e3o",
  "scheduledExecution.property.defaultTab.description":
    "Aba padr\u00e3o para apresentar quando est\u00e1 monitorando uma execu\u00e7\u00e3o",
  "scheduledExecution.property.excludeFilterUncheck.label":
    "Show Excluded Nodes",
  "scheduledExecution.property.excludeFilterUncheck.description":
    "If true, the excluded nodes will be indicated when running the Job. Otherwise they will not be shown at all.",
  "scheduledExecution.property.logOutputThreshold.label":
    "Limite de Sa\u00edda de Log",
  "scheduledExecution.property.logOutputThreshold.description":
    'Insira o n\u00famero m\u00e1ximo total de linhas (por exemplo, "100"), o n\u00famero m\u00e1ximo de linhas por n\u00f3 ("100/n\u00f3") ou o tamanho m\u00e1ximo do arquivo de log ' +
    '("100MB", "100KB", etc.), usando "GB", "MB", "KB", "B" como Giga- Mega- Kilo- e bytes.',
  "scheduledExecution.property.logOutputThreshold.placeholder":
    'Por exemplo, "100", "100/n\u00f3" ou "100MB"',
  "scheduledExecution.property.logOutputThresholdAction.label":
    "A\u00e7\u00e3o Limite de Log",
  "scheduledExecution.property.logOutputThresholdAction.description":
    "A\u00e7\u00e3o a ser executada se o limite de sa\u00edda for atingido.",
  "scheduledExecution.property.logOutputThresholdAction.halt.label":
    "Parar com status:",
  "scheduledExecution.property.logOutputThresholdAction.truncate.label":
    "Truncar e continuar",
  "scheduledExecution.property.logOutputThresholdStatus.placeholder":
    "'failed', 'aborted' ou qualquer string",
  "scheduledExecution.property.loglevel.help":
    "O n\u00edvel de Debug produz mais sa\u00eddas",
  "scheduledExecution.property.maxMultipleExecutions.label":
    "Limitar v\u00e1rias execu\u00e7\u00f5es?",
  "scheduledExecution.property.maxMultipleExecutions.description":
    "N\u00famero m\u00e1ximo de m\u00faltiplas execu\u00e7\u00f5es. Use em branco ou 0 para indicar nenhum limite.",
  "scheduledExecution.property.multipleExecutions.description":
    "Permitir que esta tarefa seja executada mais de uma vez simultaneamente?",
  "scheduledExecution.property.nodeKeepgoing.prompt": "Se um n\u00f3 falhar",
  "scheduledExecution.property.nodeKeepgoing.true.description":
    "Continue executando nos n\u00f3s restantes antes de falhar na etapa.",
  "scheduledExecution.property.nodeKeepgoing.false.description":
    "Falha no passo sem executar nos n\u00f3s restantes.",
  "scheduledExecution.property.nodeRankAttribute.label":
    "Atributo de classifica\u00e7\u00e3o",
  "scheduledExecution.property.nodeRankAttribute.description":
    "Atributo de n\u00f3 para ordenar. O padr\u00e3o \u00e9 o nome do n\u00f3.",
  "scheduledExecution.property.nodeRankOrder.label":
    "Ordem de classifica\u00e7\u00e3o",
  "scheduledExecution.property.nodeRankOrder.ascending.label": "Ascendente",
  "scheduledExecution.property.nodeRankOrder.descending.label": "descendente",
  "scheduledExecution.property.nodeThreadcount.label": "Contagem de fios",
  "scheduledExecution.property.nodeThreadcount.description":
    "N\u00famero m\u00e1ximo de encadeamentos paralelos a serem usados. (Padr\u00e3o: 1)",
  "scheduledExecution.property.nodefiltereditable.label":
    "Filtro edit\u00e1vel",
  "scheduledExecution.property.nodesSelectedByDefault.label":
    "Sele\u00e7\u00e3o de n\u00f3s",
  "scheduledExecution.property.nodesSelectedByDefault.true.description":
    "N\u00f3s de destino s\u00e3o selecionados por padr\u00e3o",
  "scheduledExecution.property.nodesSelectedByDefault.false.description":
    "O usu\u00e1rio precisa selecionar explicitamente os n\u00f3s de destino",
  "scheduledExecution.property.notifyAvgDurationThreshold.label": "Limite",
  "scheduledExecution.property.notifyAvgDurationThreshold.description":
    "Adicione ou defina um valor limite \u00e0 dura\u00e7\u00e3o m\u00e9dia para acionar essa notifica\u00e7\u00e3o. Op\u00e7\u00f5es:\n" +
    "- porcentagem => Por exemplo: 20% \n" +
    "- tempo delta => por exemplo: + 20s, +20 \n" +
    "- tempo absoluto => 30s, 5m \n" +
    "Tempo em segundos, se voc\u00ea n\u00e3o especificar unidades de tempo " +
    "Pode incluir refer\u00eancias de valor de op\u00e7\u00e3o como {'$'}{'{'}option{'.'}avgDurationThreshold{'}'}.",
  "scheduledExecution.property.orchestrator.label": "Orquestrador",
  "scheduledExecution.property.orchestrator.description":
    "Isso pode ser usado para controlar a ordem e o momento em que os n\u00f3s s\u00e3o processados",
  "scheduledExecution.property.retry.description":
    "N\u00famero m\u00e1ximo de vezes para repetir a execu\u00e7\u00e3o quando esta job \u00e9 invocada diretamente. A repeti\u00e7\u00e3o ocorrer\u00e1 se o jpb falhar ou expirar, mas n\u00e3o se for manualmente eliminado. Pode usar uma refer\u00eancia de valor de op\u00e7\u00e3o como \"{'$'}{'{'}option{'.'}retry{'}'}\".",
  "scheduledExecution.property.retry.delay.description":
    "O tempo entre a falha na execu\u00e7\u00e3o e a nova tentativa. Tempo em segundos, " +
    'ou especifique unidades de tempo: "120m", "2h", "3d". Use em branco ou 0 para indicar nenhum atraso. Pode incluir valor da op\u00e7\u00e3o ' +
    "refer\u00eancias como \"{'$'}{'{'}option{'.'}delay{'}'}\".",
  "scheduledExecution.property.successOnEmptyNodeFilter.prompt":
    "Sei18n.js o n\u00f3 estiver vazio",
  "scheduledExecution.property.successOnEmptyNodeFilter.true.description":
    "Continue a execu\u00e7\u00e3o.",
  "scheduledExecution.property.successOnEmptyNodeFilter.false.description":
    "Falha na tarefa.",
  "scheduledExecution.property.timeout.description":
    "O tempo m\u00e1ximo para uma execu\u00e7\u00e3o ser executada. Tempo em segundos, " +
    'ou especifique unidades de tempo: "120m", "2h", "3d". Use em branco ou 0 para indicar nenhum tempo limite. Pode incluir valor de refer\u00eancia ' +
    "da op\u00e7\u00e3o como \"{'$'}{'{'}option{'.'}timeout{'}'}\".",
  "scheduledExecution.property.scheduleEnabled.description":
    "Permitir que esta tarefa seja agendada?",
  "scheduledExecution.property.scheduleEnabled.label": "Ativar agendamento?",
  "scheduledExecution.property.executionEnabled.description":
    "Permitir que esta tarefa seja executada?",
  "scheduledExecution.property.executionEnabled.label":
    "Ativar Execu\u00e7\u00e3o?",
  "scheduledExecution.property.timezone.prompt": "Fuso hor\u00e1rio",
  "scheduledExecution.property.timezone.description":
    'Um fuso hor\u00e1rio v\u00e1lido, seja uma abrevia\u00e7\u00e3o como "PST", um nome completo, como "America/Los_Angeles", ou um ID personalizado, como "GMT-8{\':\'}00".',
  "documentation.reference.cron.url":
    "https{':'}//www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/crontrigger.html",
  "set.as.default.filter": "Definir como filtro padr\u00e3o",
  "show.all.nodes": "Mostrar todos os n\u00f3s",
  yes: "Sim",
  // job query field labels
  "jobquery.title.titleFilter": "Comando ad hoc",
  "jobquery.title.contextFilter": "Contexto",
  "jobquery.title.actionFilter": "A\u00e7\u00e3o",
  "jobquery.title.maprefUriFilter": "URI de recurso",
  "jobquery.title.reportIdFilter": "Nome",
  "jobquery.title.tagsFilter": "Tag",
  "jobquery.title.nodeFilter": "N\u00f3",
  "jobquery.title.nodeFilter.plural": "N\u00f3s",
  "jobquery.title.messageFilter": "mensagem",
  "jobquery.title.reportKindFilter": "Tipo de relat\u00f3rio",
  "jobquery.title.recentFilter": "Dentro",
  "jobquery.title.actionTypeFilter": "A\u00e7\u00e3o",
  "jobquery.title.itemTypeFilter": "Tipo de item",
  "jobquery.title.filter": "Filtro",
  "jobquery.title.jobFilter": "Nome do Job",
  "jobquery.title.idlist": "ID do Job",
  "jobquery.title.jobIdFilter": "ID do Job",
  "jobquery.title.descFilter": "Descri\u00e7\u00e3o da Job",
  "jobquery.title.objFilter": "Recurso",
  "jobquery.title.scheduledFilter": "Agendado",
  "jobquery.title.serverNodeUUIDFilter": "UUID do N\u00f3 do Servidor",
  "jobquery.title.typeFilter": "Tipo",
  "jobquery.title.cmdFilter": "Comando",
  "jobquery.title.userFilter": "Usu\u00e1rio",
  "jobquery.title.projFilter": "Projeto",
  "jobquery.title.statFilter": "Resultado",
  "jobquery.title.startFilter": "Hora de in\u00edcio",
  "jobquery.title.startbeforeFilter": "Come\u00e7a Antes",
  "jobquery.title.startafterFilter": "Come\u00e7a Depois",
  "jobquery.title.endbeforeFilter": "Termina Antes",
  "jobquery.title.endafterFilter": "Termina Depois",
  "jobquery.title.endFilter": "Hora",
  "jobquery.title.durationFilter": "Dura\u00e7\u00e3o",
  "jobquery.title.outFilter": "Sa\u00edda",
  "jobquery.title.objinfFilter": "Informa\u00e7\u00e3o do Recurso",
  "jobquery.title.cmdinfFilter": "Informa\u00e7\u00e3o do Comando",
  "jobquery.title.groupPath": "Grupo",
  "jobquery.title.summary": "Resumo",
  "jobquery.title.duration": "Dura\u00e7\u00e3o",
  "jobquery.title.loglevelFilter": "N\u00edvel de Log",
  "jobquery.title.loglevelFilter.label.DEBUG": "Debug",
  "jobquery.title.loglevelFilter.label.VERBOSE": "Verboso",
  "jobquery.title.loglevelFilter.label.INFO": "Informa\u00e7\u00e3o",
  "jobquery.title.loglevelFilter.label.WARN": "Aviso",
  "jobquery.title.loglevelFilter.label.ERR": "Erro",
  "jobquery.title.adhocExecutionFilter": "Tipo de tarefa",
  "jobquery.title.adhocExecutionFilter.label.true": "Comando",
  "jobquery.title.adhocExecutionFilter.label.false": "Comando Definido",
  "jobquery.title.adhocLocalStringFilter": "Conte\u00fado do Script",
  "jobquery.title.adhocRemoteStringFilter": "Comando Shell",
  "jobquery.title.adhocFilepathFilter": "Caminho do arquivo script",
  "jobquery.title.argStringFilter": "Par\u00e2metros do Arquivo de Script",
  "page.unsaved.changes": "You have unsaved changes",
  "edit.nodes.file": "Edit Nodes File",
  "project.node.file.source.label": "Source",
  "file.display.format.label": "Format",
  "project.node.file.source.description.label": "Description",
  "project.nodes.edit.save.error.message": "Error Saving Content:",
  "project.nodes.edit.empty.description": "Note: No content was available.",
  "button.action.Cancel": "Cancel",
  "button.action.Save": "Save",
};

export default messages;
