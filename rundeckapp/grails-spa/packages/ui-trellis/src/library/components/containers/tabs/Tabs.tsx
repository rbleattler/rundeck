import {h, defineComponent, VNode} from 'vue'

import './tabs.scss'
import './tabs-standard.scss'
import './tabs-rounded.scss'

interface Tab {
    title: string
    active: boolean
    index: number
    keep: boolean
}

export default defineComponent({
    name: 'rd-tabs',
    props: {
        type: {type: String, default: 'standard'},
        active: {type: Number, default: 0},
        label: {type: String, default: 'Tabs'},
    },
    created() {
        this.activeTab = this.active
    },
    data: () => ({
        activeTab: 0
    }),
    methods: {
        handleSelect(tab: Tab) {
            this.activeTab = tab.index
        },
        handleKeypress(ev: KeyboardEvent, tab: Tab) {
            if (ev.code == 'Space')
                this.handleSelect(tab)
        },
        tabs(): Array<VNode> {
            if(this.$slots && this.$slots.default) {
                return (this.$slots.default().filter(s => {
                    const tab = s
                    return tab
                }) || [])
            }
            return []
        }
    },
    render() {
        // TODO refactor
        const activeNode = this.tabs()[this.activeTab]

        this.tabs().forEach((t, i) => t.key = i.toString())

        const _activeTab = activeNode.props as any as Tab

        const childrenTabs = this.tabs().map((node, i) => {
            const tab = node.props as any as Tab
            return h(
                <div class={{
                    "rdtabs__tab": true,
                    "rdtabs__tab-component": true,
                    "rdtabs__tab--active": i == this.activeTab,
                    "rdtabs__tab-previous": i == this.activeTab - 1
                }}
                     role="tab" aria-selected={i == this.activeTab}
                     key={tab.index}
                     tabindex="0"
                     onClick={() => {this.handleSelect(tab)}}
                     onKeypress={(ev: KeyboardEvent) => {this.handleKeypress(ev, tab)}}>
                    <div class="rdtabs__tab-inner">
                        {tab.title}
                    </div>
                </div>
            )
        });

        return h(
            <div class={["rdtabs", `rdtabs--${this.type}`]}>
                <div class={`rdtabs__tabheader rdtabs__tabheader--${this.type}`}>
                    <div class="rdtabs__tabheader-inner">
                    <div class={["rdtabs__tablist", `rdtabs__tablist--${this.type}`]} role="tablist" aria-label={this.label}>
                        <div class={{
                            "rdtabs__leftendcap": true,
                            "rdtabs__tab-component": true,
                            "rdtabs__tab-previous": this.activeTab == 0
                        }}><div class="rdtabs__tab-inner"/></div>
                        {childrenTabs}
                        <div class={{"rdtabs__rightendcap": true, "rdtabs__tab-component": true}}><div class="rdtabs__tab-inner"/></div>
                    </div>
                    </div>
                </div>
                <div class="rdtabs__pane" role="tabpanel">
                    {
                        _activeTab.keep != false ?
                            <keep-alive>{activeNode}</keep-alive> :
                            activeNode
                    }
                </div>
            </div>
        )
    }
})
