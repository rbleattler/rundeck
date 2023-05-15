
<template>
    <div id="fieldcustomeditor" class="col-sm-12">
        <hr/>

        <div v-if="customFields!=null">
            <div v-for="(field, index) in customFields" :key="index" :class="['form-group']" >
                <label class="col-sm-2 control-label input-sm">{{ field.label||field.key }}</label>
                <div class="col-sm-9">
                    <input  v-model="field.value" type="text" :class="['form-control','input-sm','context_var_autocomplete']" size="100" @change="changeField(field)" >
                </div>
                <div class="col-sm-1">
                <span class="btn btn-xs btn-default " @click="removeField(field)" :title="t('message.delete')">
                <i class="glyphicon glyphicon-remove"></i></span>

                </div>
                <div class="col-sm-10 col-sm-offset-2 help-block" v-if="field.desc">
                    <div class="help-block">{{field.desc}}</div>
                </div>
            </div>
        </div>

        <btn type="primary" @click="openNewField()">{{ t('message.addField') }}</btn>

        <modal v-model="modalAddField" title="Add Field" ref="modal" id="modal-demo" ok-text="Save" :backdrop="true"
               :dismiss-btn="true"
               :keyboard="true"
               cancel-text="Close" append-to-body>
            <div class="row" style="padding-left: 30px !important;">
                <alert type="warning" v-if="duplicate"><b>Warning!</b> {{ t('message.duplicated') }}.</alert>


                <div class="col-md-10">
                    <div class="form"  v-if="useOptions">
                        <div :class="['form-data']">
                            <label class="col-md-4">{{ t('message.select') }}</label>
                            <div class="col-md-8">

                              <multiselect v-model="selectedField" :options="customOptions"  track-by="label" label="label"
                                           :placeholder="t('message.select' )">

                              </multiselect>


                            </div>
                        </div>

                        <div :class="['form-data']" >
                            <label class="col-md-4">{{ t('message.description') }}</label>
                            <div class="col-md-8">
                                <input type="text" v-model="newFieldDescription" :class="['form-control']">
                                <div class="help-block">{{ t('message.empty') }}</div>
                            </div>
                        </div>
                    </div>

                    <div class="form" v-if="!useOptions">
                        <div :class="['form-group']">
                            <label class="col-md-4">{{ t('message.fieldLabel') }}</label>
                            <div class="col-md-8">
                                <input type="text" v-model="newLabelField" :class="['form-control']">
                            </div>
                        </div>
                        <div :class="['form-group']" >
                            <label class="col-md-4">{{ t('message.fieldKey') }}</label>
                            <div class="col-md-8">
                                <input type="text" v-model="newField" :class="['form-control']">
                            </div>
                        </div>

                        <div :class="['form-group']" >
                            <label class="col-md-4">{{ t('message.description') }}</label>
                            <div class="col-md-8">
                                <input type="text" v-model="newFieldDescription" :class="['form-control']">
                                <div class="help-block">{{ t('message.empty') }}</div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>


          <template v-slot:footer>
            <div>
                <button type="button" class="btn btn-default reset_page_confirm" @click="modalAddField=false">
                    {{ t('message.cancel') }}
                </button>

                <button type="button" class="btn btn-cta reset_page_confirm" @click="addField()">
                    {{ t('message.add') }}
                </button>
            </div>
          </template>
        </modal>
    </div>
</template>

<script lang="ts">
import VueMultiselect from 'vue-multiselect'
import { ref, defineComponent} from "vue";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'DynamicFormPluginProp',
  components: {
    VueMultiselect
  },
  props: {
    fields: {
      type: String,
      required: true
    },
    options: {
      type: String,
      required: false
    },
    element: {
      type: String,
      required: true
    },
    hasOptions: {
      type: String,
      required: true
    },
    name: {
      type: String,
      required: true
    }
  },
  emits: ['input'],
  setup() {
    let customFields = ref<any[]>([])
    let customOptions = ref<any[]>([])
    let useOptions = ref(false)
    let modalAddField = ref(false)
    let duplicate = ref(false)
    let newField = ref('')
    let newLabelField = ref('')
    let newFieldDescription = ref('')
    let selectedField = {value: "", label: ""} as any
    const { t, locale } = useI18n({
      useScope: 'global',
    })
    return {
      customFields,
      customOptions,
      useOptions,
      modalAddField,
      duplicate,
      newField,
      newLabelField,
      newFieldDescription,
      selectedField,
      t,
      locale
    }
  },
  methods: {
    openNewField() {
      this.modalAddField.value = true;
    },
    addField() {
      let field = {} as any;
      this.duplicate.value = false;

      if (this.useOptions.value) {

        if (this.selectedField !== null) {

          const newField = this.selectedField;

          let description = this.newFieldDescription.value;
          if (description == '') {
            description = 'Field key ' + newField.value
          } else {
            description = description + ' (Field key: ' + newField.value + ')';
          }

          field = {key: newField.value, label: newField.label, desc: description};

        }
      } else {
        let description = this.newFieldDescription.value;
        if (description == '') {
          description = 'Field key ' + this.newField
        } else {
          description = description + ' (Field key: ' + this.newField + ')';
        }

        field = {key: this.newField, label: this.newLabelField, value: '', desc: description}
      }

      let exists = false;
      this.customFields.value.forEach((row: any) => {
        if (field.key === row.key) {
          exists = true;
        }
      });


      if (!exists) {
        this.customFields.value.push(field);
        this.newField.value = '';
        this.newLabelField.value = '';
        this.newFieldDescription.value = '';
        this.modalAddField.value = false;
        this.refreshPlugin();
      } else {
        this.duplicate.value = true;
      }
    },
    removeField(row: any) {
      const fields = [] as any;
      this.customFields.value.forEach((field: any) => {
        if (field.key !== row.key) {
          fields.push(field);
        }
      });
      this.customFields.value = fields;
      this.refreshPlugin();
    },
    changeField(field: any) {
      this.refreshPlugin();
    },
    refreshPlugin() {
      const fieldsJson = JSON.stringify(this.customFields);
      const cFields = document.getElementById(this.props.element) as HTMLInputElement;
      cFields.value = fieldsJson;
      this.$emit('input', fieldsJson);
    }
  },
  mounted() {
    if (this.props.hasOptions === 'true') {
      this.useOptions.value = true
    }
    if (this.props.fields != null && this.props.fields !== '') {
      const customFieldsObject = JSON.parse(this.props.fields);
      if (customFieldsObject != null) {
        const fields = Object.keys(customFieldsObject).map((key: any) => {
          const value = customFieldsObject[key];
          if (value.desc == null) {
            value.desc = 'Field key: ' + value.key
          }
          return value;
        });
        this.customFields.value = fields;
      }
    }

    if (this.props.hasOptions && this.props.options != null && this.props.options !== '') {
      const optionsObject = JSON.parse(this.props.options);
      const options = Object.keys(optionsObject).map((key: any) => {
        const data = optionsObject[key];
        return {value: key, label: data};
      });
      this.customOptions.value = options;
    }
  },
  beforeUnmount() {
    this.customFields.value = null as any;
  },
})


</script>
<style scoped src="vue-multiselect/dist/vue-multiselect.min.css"></style>

<style scoped>

.form-data{
  padding-bottom: 30px;
  margin-bottom: 20px;
}
</style>
