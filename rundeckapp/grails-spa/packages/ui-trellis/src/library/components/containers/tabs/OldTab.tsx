import Vue, {h, defineComponent, VNode} from 'vue'

export default defineComponent({
    name: 'old-rd-tab',
    props: {
        title: String,
        index: Number,
        active: {type: Boolean, default: false},
        keep: {type: Boolean, default: true}
    },
    render() {
        let component: VNode
        if (! this.$slots.default?.length)
            component = h('div')
        else if (this.$slots.default.length > 1)
            component = h('div', this.$slots.default)
        else
            component = this.$slots.default()[0]

        return component
    }
})