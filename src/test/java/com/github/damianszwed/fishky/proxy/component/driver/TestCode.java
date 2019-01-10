package com.github.damianszwed.fishky.proxy.component.driver;

@FunctionalInterface
public interface TestCode {

  void run(FishkyProxyDriver driver) throws Exception;
}
