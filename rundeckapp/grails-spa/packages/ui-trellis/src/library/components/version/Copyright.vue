<template>
    <span class="copyright">
        {{ copyright }}
    </span>
</template>

<script>
import * as AppConstants from "@/app/utilities/AppConstants";

export default {
    data () {
        return {
            copyright: AppConstants.buildCopyright
        }
    }
}
</script>

<style scoped lang="scss">
.copyright {
    color: var(--font-color);
}
</style>