<template>
  <div>
    <div class="form-horizontal">
      <plugin-config
        v-if="isConfigSet"
        v-model="cacheConfig"
        mode="create"
        :show-title="false"
        :show-description="false"
        :use-runner-selector="true"
        :event-bus="rundeckContext.eventBus"
        :plugin-config="pluginConfig"
        :category="category"
      >
        <template #extra>
          <div class="row">
            <div class="col-xs-12 col-sm-12">
              <span>
                <a
                  key="save"
                  class="btn btn-cta reset_page_confirm"
                  @click="saveConfig"
                  >{{ "Save" }}</a
                >
              </span>
            </div>
          </div>
        </template>
      </plugin-config>
    </div>
  </div>
</template>

<script lang="ts">
import PluginConfig from "../../../library/components/plugins/pluginConfig.vue";
import { getRundeckContext, RundeckContext } from "../../../library";
import {
  getProjectConfigurable,
  setProjectConfigurable,
} from "./nodeSourcesUtil";
import { Notification } from "uiv";
import { defineComponent, onMounted, ref } from "vue";
import { PropType } from "vue/dist/vue";

interface Prop {
  type: string;
  defaultValue: any;
  title: string;
  required: boolean;
  options: any;
  allowed: string;
  name: string;
  desc: string;
  staticTextDefaultValue: string;
}

interface NodeServiceConfigurableItemValues {
  delay: string;
  enabled: string;
  firstLoadSynch: string;
}
interface HealthCheckConfigurableItemValues {
  onstartup: string;
  enabled: string;
  period: string;
  refreshHealthCheckCache: string;
}
interface ConfigurableItem {
  name: string;
  properties: [Prop];
  propertiesMapping:
    | NodeServiceConfigurableItemValues
    | HealthCheckConfigurableItemValues;
  values: NodeServiceConfigurableItemValues | HealthCheckConfigurableItemValues;
}

export default defineComponent({
  name: "ProjectConfigurableForm",
  components: {
    PluginConfig,
  },
  props: {
    category: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      rundeckContext: {} as RundeckContext,
      pluginConfig: {},
      apiResponse: {},
      isConfigSet: false,
      cacheConfig: { config: {} },
      massagedConfig: {},
    };
  },
  async mounted() {
    await this.loadConfig();
  },
  methods: {
    async loadConfig() {
      try {
        const config = await getProjectConfigurable(
          window._rundeck.projectName,
          this.category,
        );
        this.apiResponse = config.response["projectConfigurable"] as [
          ConfigurableItem,
        ];
        const properties = [];
        this.apiResponse.forEach((item: ConfigurableItem) => {
          const itemName = item.name;
          item.properties.forEach((prop: any) => {
            const originalPropName = prop.name;
            prop.name = itemName + "." + prop.name;
            Object.entries(item.values).forEach(([key, value]) => {
              if (originalPropName === key) {
                this.massagedConfig[prop.name] = value;
              }
            });
          });
          properties.push(item.properties);
        });
        const resolvedProps = [];
        properties.forEach((item: any) => {
          item.forEach((prop: any) => {
            prop.desc = prop.description;
            resolvedProps.push({ ...prop, options: prop.renderingOptions });
          });
        });
        this.pluginConfig = {
          type: "ResourceModelSource",
          props: resolvedProps,
        };
        this.cacheConfig.config = this.massagedConfig;
        this.isConfigSet = true;
      } catch (err) {
        this.notifyError("Error loading config: " + err.message);
        console.error(err);
      }
    },
    async saveConfig() {
      const configsToSave = this.convertMapNumbersToStrings(
        this.transformConfigToMap(this.cacheConfig.config),
      );
      try {
        const resp = await setProjectConfigurable(
          window._rundeck.projectName,
          this.category,
          configsToSave,
        );
        if (resp.response === true) {
          this.notifySuccess("Success!", "Config saved successfully");
          await this.loadConfig();
        }
      } catch (err) {
        console.error(err);
        this.notifyError("Error saving config: " + err.message);
      }
    },
    convertMapNumbersToStrings(obj: any): any {
      for (const key in obj) {
        if (typeof obj[key] === "number") {
          // Convert number to string
          obj[key] = obj[key].toString();
        } else if (typeof obj[key] === "object" && obj[key] !== null) {
          // Recursively convert numbers to strings in nested objects
          obj[key] = this.convertMapNumbersToStrings(obj[key]);
        }
      }
      return obj;
    },
    transformConfigToMap(config: any) {
      const resultMap = {};

      Object.entries(config).forEach(([key, value]) => {
        const [mainKey, subKey] = key.split(".");
        if (!resultMap[mainKey]) {
          resultMap[mainKey] = {};
        }
        resultMap[mainKey][subKey] = value;
      });

      return resultMap;
    },
    notifyError(msg: string) {
      Notification.notify({
        type: "danger",
        title: "An Error Occurred",
        content: msg,
        duration: 0,
      });
    },

    notifySuccess(title: string, msg: string) {
      Notification.notify({
        type: "success",
        title: title,
        content: msg,
        duration: 5000,
      });
    },
  },
});
</script>

<style></style>
