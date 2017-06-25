import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AgePipe} from "./age.pipe";
import {MemoryPipe} from "./memory.pipe";
import {KvPipe} from "./kv.pipe";

@NgModule({
    imports: [
        CommonModule
    ],
    declarations: [
        AgePipe,
        MemoryPipe,
        KvPipe
    ],
    exports: [
        AgePipe,
        MemoryPipe,
        KvPipe
    ]
})
export class PipesModule {
}
