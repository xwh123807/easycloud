var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Component, Input, Output, EventEmitter } from '@angular/core';
var StatComponent = (function () {
    function StatComponent() {
        this.event = new EventEmitter();
    }
    StatComponent.prototype.ngOnInit = function () { };
    return StatComponent;
}());
__decorate([
    Input(),
    __metadata("design:type", String)
], StatComponent.prototype, "bgClass", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], StatComponent.prototype, "icon", void 0);
__decorate([
    Input(),
    __metadata("design:type", Number)
], StatComponent.prototype, "count", void 0);
__decorate([
    Input(),
    __metadata("design:type", String)
], StatComponent.prototype, "label", void 0);
__decorate([
    Input(),
    __metadata("design:type", Number)
], StatComponent.prototype, "data", void 0);
__decorate([
    Output(),
    __metadata("design:type", EventEmitter)
], StatComponent.prototype, "event", void 0);
StatComponent = __decorate([
    Component({
        selector: 'app-stat',
        templateUrl: './stat.component.html',
        styleUrls: ['./stat.component.scss']
    }),
    __metadata("design:paramtypes", [])
], StatComponent);
export { StatComponent };
//# sourceMappingURL=stat.component.js.map