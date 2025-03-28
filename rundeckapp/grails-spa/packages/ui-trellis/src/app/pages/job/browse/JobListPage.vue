<template>
  <JobBulkEditControls v-if="showBulkEdit" />
  <Browser
    v-if="loaded"
    :path="jobPageStore.browsePath"
    :root="true"
    class="job_list_browser"
    :expand-level="jobPageStore.groupExpandLevel"
    :query-refresh="queryRefresh"
    @root-browse="rootBrowse"
  >
    <ui-socket section="job-list-page" location="empty-splash">
      <div v-if="showCreateButton" class="empty-splash">
        <create-new-job-button btn-type="cta">
          {{ $t("job.create.button") }}
        </create-new-job-button>
        <upload-job-button></upload-job-button>
      </div>
      <div v-else>
        <p>{{ $t("no.jobs.in.this.project") }}</p>
      </div>
    </ui-socket>
  </Browser>
</template>

<script lang="ts">
import CreateNewJobButton from "@/app/pages/job/browse/components/CreateNewJobButton.vue";
import UploadJobButton from "@/app/pages/job/browse/components/UploadJobButton.vue";
import JobBulkEditControls from "@/app/pages/job/browse/JobBulkEditControls.vue";
import { getRundeckContext } from "@/library";
import UiSocket from "@/library/components/utils/UiSocket.vue";
import {
  JobBrowserStore,
  JobBrowserStoreInjectionKey,
} from "@/library/stores/JobBrowser";
import {
  JobPageStore,
  JobPageStoreInjectionKey,
} from "@/library/stores/JobPageStore";
import { defineComponent, inject, ref } from "vue";
import Browser from "./tree/Browser.vue";
const eventBus = getRundeckContext().eventBus;
const currentProject = getRundeckContext().projectName;
export default defineComponent({
  name: "JobListPage",
  components: {
    UploadJobButton,
    UiSocket,
    CreateNewJobButton,
    JobBulkEditControls,
    Browser,
  },
  provide() {
    return {
      allowFolderNavigation: this.allowFolderNavigation,
    };
  },
  props: {
    path: {
      type: String,
      default: "",
    },
    showBulkEdit: {
      type: Boolean,
      default: true,
    },
    showCreateButton: {
      type: Boolean,
      default: true,
    },
    allowFolderNavigation: {
      type: Boolean,
      default: true,
    },
    projectToDisplay: {
      type: String,
      default: "",
    },
  },
  setup(props) {
    const jobBrowserStore: JobBrowserStore = inject(
      JobBrowserStoreInjectionKey,
    ) as JobBrowserStore;
    const jobPageStore: JobPageStore = inject(
      JobPageStoreInjectionKey,
    ) as JobPageStore;
    return {
      jobBrowserStore,
      jobPageStore,
      loaded: ref(false),
      queryRefresh: ref(false),
    };
  },
  async mounted() {
    eventBus.on("job-list-page:search", async () => {
      if (
        this.jobPageStore.query["groupPath"] !== this.jobPageStore.browsePath
      ) {
        this.jobPageStore.browsePath =
          this.jobPageStore.query["groupPath"] || "";
        await this.jobBrowserStore.reload();
        await this.rootBrowse(this.jobPageStore.browsePath, null);
      } else {
        await this.jobBrowserStore.reload();
        this.queryRefresh = !this.queryRefresh;
      }
    });
    eventBus.on("job-list-page:rootBrowse", async (evt) => {
      const { path, href } = evt;
      this.rootBrowse(path, href);
    });
    if (typeof history.replaceState == "function") {
      if (!history.state) {
        //set first page load state
        const state = this.jobPageStore.browsePath
          ? { start: true, browsePath: this.jobPageStore.browsePath }
          : { start: true };
        history.replaceState(state, null, document.location.toString());
      }
    }
    window.onpopstate = (event) => {
      if (event.state) {
        this.rootBrowse(event.state.browsePath || "", null);
      }
    };
    let projectToLoad = currentProject;

    if (this.projectToDisplay.length > 0) {
      projectToLoad = this.projectToDisplay;

      // if a project name was passed as a prop, and it's not currently stored in the jobPageStore
      // we want to force the jobBrowserStore to reload in order to fetch the jobs for the project name provided
      if (
        this.jobBrowserStore.loaded &&
        projectToLoad !== this.jobPageStore.getProject()
      ) {
        await this.jobBrowserStore.reload();
      }
    }

    await this.jobPageStore.load(projectToLoad);
    this.loaded = true;
  },
  methods: {
    async rootBrowse(path: string, href: string) {
      //deselect any jobs
      this.jobPageStore.selectedJobs = [];
      this.jobPageStore.browsePath = path;
      this.jobPageStore.query["groupPath"] = path;
      eventBus.emit("job-list-page:browsed", path);
      if (href) {
        window.history.pushState({ browsePath: path }, document.title, href);
      }
    },
  },
});
</script>

<style scoped lang="scss">
.empty-splash {
  .btn + .btn {
    margin-left: var(--spacing-4);
  }
}
</style>
