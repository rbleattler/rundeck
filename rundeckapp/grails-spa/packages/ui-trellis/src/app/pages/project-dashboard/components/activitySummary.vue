<template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card">
        <div class="card-content">
          <a class="h4" :href="`${rdBase}project/${project.name}/activity`">
            <span
              class="summary-count"
              :class="{ 'text-strong': count < 1, 'text-info': count > 0 }"
            >
              {{ count }}
            </span>
            {{ $t("execution", count) }}
          </a>

          {{ $t("In the last Day") }}

          <span v-if="project.failedCount > 0" data-test-id="failed-count">
            <a
              :class="{
                'text-warning': project.failedCount > 0,
                'text-muted': project.failedCount < 1,
              }"
              :href="`${rdBase}project/${project.name}/activity?statFilter=fail`"
            >
              {{
                $t("project.activitySummary.failedCount", [project.failedCount])
              }}
            </a>
          </span>
          <div v-if="project.userCount > 0" data-test-id="user-count">
            <i18n-t keypath="project.activitySummary.userCount" tag="p">
              <template #count>
                <span class="text-info">{{ project.userCount }}</span>
              </template>
              <template #users>
                {{ $t("users.plural", project.userCount) }}
              </template>
            </i18n-t>
            <ul class="users">
              <li v-for="user in project.userSummary" :key="user">
                {{ user }}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Activity",
  props: ["project", "rdBase"],
  data() {
    return {
      count: 0,
    };
  },
  computed: {
    pluralUsers() {
      return this.project.count === 1 ? "User" : "Users";
    },
  },
  mounted() {
    this.count = this.project.execCount;
  },
};
</script>

<style scoped lang="scss">
ul.users {
  display: inline;
  margin: 0;
  padding: 0;
}

ul.users li {
  display: inline;
  list-style: none;
  margin: 0;
  padding: 0;
}

ul.users li:after {
  content: ", ";
  color: #aaa;
}

ul.users li:last-child:after {
  content: "";
}
</style>
