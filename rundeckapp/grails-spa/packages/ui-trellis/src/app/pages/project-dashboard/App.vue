<template>
  <div v-if="project" id="app">
    <slot :project="project"></slot>
    <activity-summary
      v-if="eventsAuth && project && showSummary !== 'false'"
      :project="project"
      :rd-base="rdBase"
    ></activity-summary>
    <project-readme
      v-if="project && showReadme !== 'false'"
      :project="project"
    ></project-readme>
  </div>
</template>

<script>
import projectDescription from "./components/description.vue";
import projectReadme from "./components/projectReadme.vue";
import activitySummary from "./components/activitySummary.vue";
import activityList from "../../components/activity/activityList.vue";

import { getRundeckContext } from "../../../library";

export default {
  name: "App",
  components: {
    // motd,
    projectDescription,
    projectReadme,
    activitySummary,
    activityList,
  },
  props: ["eventBus", "showDescription", "showReadme", "showSummary"],
  data() {
    return {
      project: null,
      rdBase: null,
      eventsAuth: false,
    };
  },
  async mounted() {
    if (
      window._rundeck &&
      window._rundeck.rdBase &&
      window._rundeck.projectName
    ) {
      this.rdBase = window._rundeck.rdBase;
      this.eventsAuth =
        window._rundeck.data && window._rundeck.data.projectEventsAuth;
      const response = await getRundeckContext().rundeckClient.sendRequest({
        method: "get",
        pathTemplate: "/menu/homeAjax",
        baseUrl: this.rdBase,
        queryParameters: {
          projects: window._rundeck.projectName,
        },
      });
      if (response.parsedBody.projects) {
        this.project = response.parsedBody.projects[0];
      }
    }
  },
};
</script>

<style></style>
