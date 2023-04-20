
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
                <span class="btn btn-xs btn-default " @click="removeField(field)" :title="$t('message.delete')">
                <i class="glyphicon glyphicon-remove"></i></span>

                </div>
                <div class="col-sm-10 col-sm-offset-2 help-block" v-if="field.desc">
                    <div class="help-block">{{field.desc}}</div>
                </div>
            </div>
        </div>

        <btn type="primary" @click="openNewField()">{{ $t('message.addField') }}</btn>

        <modal v-model="modalAddField" title="Add Field" ref="modal" id="modal-demo" ok-text="Save" :backdrop="true"
               :dismiss-btn="true"
               :keyboard="true"
               cancel-text="Close" append-to-body>
            <div class="row" style="padding-left: 30px !important;">
                <alert type="warning" v-if="duplicate"><b>Warning!</b> {{ $t('message.duplicated') }}.</alert>


                <div class="col-md-10">
                    <div class="form"  v-if="useOptions">
                        <div :class="['form-data']">
                            <label class="col-md-4">{{ $t('message.select') }}</label>
                            <div class="col-md-8">

                              <multiselect v-model="selectedField" :options="customOptions"  track-by="label" label="label"
                                           :placeholder="$t('message.select' )">

                              </multiselect>


                            </div>
                        </div>

                        <div :class="['form-data']" >
                            <label class="col-md-4">{{ $t('message.description') }}</label>
                            <div class="col-md-8">
                                <input type="text" v-model="newFieldDescription" :class="['form-control']">
                                <div class="help-block">{{ $t('message.empty') }}</div>
                            </div>
                        </div>
                    </div>

                    <div class="form" v-if="!useOptions">
                        <div :class="['form-group']">
                            <label class="col-md-4">{{ $t('message.fieldLabel') }}</label>
                            <div class="col-md-8">
                                <input type="text" v-model="newLabelField" :class="['form-control']">
                            </div>
                        </div>
                        <div :class="['form-group']" >
                            <label class="col-md-4">{{ $t('message.fieldKey') }}</label>
                            <div class="col-md-8">
                                <input type="text" v-model="newField" :class="['form-control']">
                            </div>
                        </div>

                        <div :class="['form-group']" >
                            <label class="col-md-4">{{ $t('message.description') }}</label>
                            <div class="col-md-8">
                                <input type="text" v-model="newFieldDescription" :class="['form-control']">
                                <div class="help-block">{{ $t('message.empty') }}</div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>


          <template v-slot:footer>
            <div>
                <button type="button" class="btn btn-default reset_page_confirm" @click="modalAddField=false">
                    {{ $t('message.cancel') }}
                </button>

                <button type="button" class="btn btn-cta reset_page_confirm" @click="addField()">
                    {{ $t('message.add') }}
                </button>
            </div>
          </template>
        </modal>
    </div>
</template>

<script setup lang="ts">
//@ts-ignore
import VueMultiselect from 'vue-multiselect'
import {onMounted, onUnmounted, ref} from "vue";

const props = defineProps({
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
})

let customFields = ref<any[]>([])
let customOptions = ref<any[]>([])
let useOptions = ref(false)
let modalAddField = ref(false)
let duplicate = ref(false)
let newField = ref('')
let newLabelField = ref('')
let newFieldDescription = ref('')
let selectedField = {value:"", label:""} as any

function openNewField() {
    modalAddField.value =true;
}

function addField() {
    let field = {} as any;
    duplicate.value = false;

    if(useOptions.value){

        if(selectedField !== null){

            const newField = selectedField;

            let description = newFieldDescription.value;
            if(description == ''){
                description = 'Field key ' + newField.value
            }else{
                description = description + ' (Field key: ' + newField.value + ')';
            }

            field = {key: newField.value, label: newField.label, desc: description } ;

        }
    }else {
        let description = newFieldDescription.value;
        if(description == ''){
            description = 'Field key ' + newField
        }else{
            description = description + ' (Field key: ' + newField + ')';
        }

        field = {key: newField, label: newLabelField, value: '', desc: description}
    }

    let exists = false;
    customFields.value.forEach((row: any) => {
        if (field.key === row.key) {
            exists = true;
        }
    });


    if(!exists){
        customFields.value.push(field);
        newField.value = '';
        newLabelField.value = '';
        newFieldDescription.value = '';
        modalAddField.value = false;

        refreshPlugin();
    }else{
        duplicate.value = true;
    }

}

function removeField(row: any) {
    const fields = [] as any;

    customFields.value.forEach((field: any) => {
        if (field.key !== row.key) {
            fields.push(field);
        }
    });

    customFields.value = fields;
    refreshPlugin();
}

function changeField(field: any){
    refreshPlugin();
}

const emit = defineEmits(['input'])
function refreshPlugin(){
    const fieldsJson = JSON.stringify(customFields);
    const cFields = document.getElementById(props.element) as HTMLInputElement;
    cFields.value = fieldsJson;

    emit('input', fieldsJson);
}

onMounted(() =>{
    if(props.hasOptions === 'true' ){
        useOptions.value = true
    }
    if(props.fields!=null && props.fields !== '' ){
        const customFieldsObject =  JSON.parse(props.fields);
        if(customFieldsObject != null){
            const fields = Object.keys(customFieldsObject).map((key: any) => {
                const value = customFieldsObject[key];
                if (value.desc == null){
                    value.desc = 'Field key: ' + value.key
                }
                return value;
            });
            customFields.value = fields;
        }
    }

    if(props.hasOptions && props.options!=null && props.options !== '' ) {
        const optionsObject =  JSON.parse(props.options);
        const options = Object.keys(optionsObject).map((key: any) => {
            const data = optionsObject[key];
            return {value: key, label: data};
        });
        customOptions.value = options;
    }
})

onUnmounted(() => {
    customFields.value = null as any;
})


</script>
<style scoped src="vue-multiselect/dist/vue-multiselect.min.css"></style>

<style scoped>

.form-data{
  padding-bottom: 30px;
  margin-bottom: 20px;
}
</style>
