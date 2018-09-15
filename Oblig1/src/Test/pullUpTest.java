package Test;

import static org.junit.Assert.*;

import org.junit.Test;

import pullUp.A;

class pullUpTest {
	A a = new A();	

	@Test
	void subClassAFunction_ShouldReturnSuperDuperClass() {
		assertEquals("SuperDuperFunction", a.function());
	}

}