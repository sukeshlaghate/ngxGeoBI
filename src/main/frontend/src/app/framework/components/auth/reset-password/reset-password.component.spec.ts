import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResetPasswordrdComponent } from './reset-password.component';

describe('ResetPasswordrdComponent', () => {
  let component: ResetPasswordrdComponent;
  let fixture: ComponentFixture<ResetPasswordrdComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResetPasswordrdComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResetPasswordrdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
