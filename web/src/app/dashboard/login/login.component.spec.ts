import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { provideRouter } from '@angular/router';
import { Component } from '@angular/core';
import { LoginFormComponent } from './login-form/login-form.component';

@Component({
  selector: 'app-login-form',
  standalone: true,
  template: '',
})
export class MockLoginFormComponent {}

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [provideRouter([])],
    })
      .overrideComponent(LoginComponent, {
        remove: { imports: [LoginFormComponent] },
        add: { imports: [MockLoginFormComponent] },
      })
      .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
