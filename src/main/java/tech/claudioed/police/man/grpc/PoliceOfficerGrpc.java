package tech.claudioed.police.man.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: policeman.proto")
public final class PoliceOfficerGrpc {

  private PoliceOfficerGrpc() {}

  public static final String SERVICE_NAME = "policies.PoliceOfficer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<tech.claudioed.police.man.grpc.MessageData,
      tech.claudioed.police.man.grpc.RegistryID> getRegistryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Registry",
      requestType = tech.claudioed.police.man.grpc.MessageData.class,
      responseType = tech.claudioed.police.man.grpc.RegistryID.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<tech.claudioed.police.man.grpc.MessageData,
      tech.claudioed.police.man.grpc.RegistryID> getRegistryMethod() {
    io.grpc.MethodDescriptor<tech.claudioed.police.man.grpc.MessageData, tech.claudioed.police.man.grpc.RegistryID> getRegistryMethod;
    if ((getRegistryMethod = PoliceOfficerGrpc.getRegistryMethod) == null) {
      synchronized (PoliceOfficerGrpc.class) {
        if ((getRegistryMethod = PoliceOfficerGrpc.getRegistryMethod) == null) {
          PoliceOfficerGrpc.getRegistryMethod = getRegistryMethod =
              io.grpc.MethodDescriptor.<tech.claudioed.police.man.grpc.MessageData, tech.claudioed.police.man.grpc.RegistryID>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Registry"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  tech.claudioed.police.man.grpc.MessageData.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  tech.claudioed.police.man.grpc.RegistryID.getDefaultInstance()))
              .setSchemaDescriptor(new PoliceOfficerMethodDescriptorSupplier("Registry"))
              .build();
        }
      }
    }
    return getRegistryMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PoliceOfficerStub newStub(io.grpc.Channel channel) {
    return new PoliceOfficerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PoliceOfficerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new PoliceOfficerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PoliceOfficerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new PoliceOfficerFutureStub(channel);
  }

  /**
   */
  public static abstract class PoliceOfficerImplBase implements io.grpc.BindableService {

    /**
     */
    public void registry(tech.claudioed.police.man.grpc.MessageData request,
        io.grpc.stub.StreamObserver<tech.claudioed.police.man.grpc.RegistryID> responseObserver) {
      asyncUnimplementedUnaryCall(getRegistryMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRegistryMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                tech.claudioed.police.man.grpc.MessageData,
                tech.claudioed.police.man.grpc.RegistryID>(
                  this, METHODID_REGISTRY)))
          .build();
    }
  }

  /**
   */
  public static final class PoliceOfficerStub extends io.grpc.stub.AbstractStub<PoliceOfficerStub> {
    private PoliceOfficerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PoliceOfficerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PoliceOfficerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PoliceOfficerStub(channel, callOptions);
    }

    /**
     */
    public void registry(tech.claudioed.police.man.grpc.MessageData request,
        io.grpc.stub.StreamObserver<tech.claudioed.police.man.grpc.RegistryID> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegistryMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PoliceOfficerBlockingStub extends io.grpc.stub.AbstractStub<PoliceOfficerBlockingStub> {
    private PoliceOfficerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PoliceOfficerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PoliceOfficerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PoliceOfficerBlockingStub(channel, callOptions);
    }

    /**
     */
    public tech.claudioed.police.man.grpc.RegistryID registry(tech.claudioed.police.man.grpc.MessageData request) {
      return blockingUnaryCall(
          getChannel(), getRegistryMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PoliceOfficerFutureStub extends io.grpc.stub.AbstractStub<PoliceOfficerFutureStub> {
    private PoliceOfficerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PoliceOfficerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PoliceOfficerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PoliceOfficerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<tech.claudioed.police.man.grpc.RegistryID> registry(
        tech.claudioed.police.man.grpc.MessageData request) {
      return futureUnaryCall(
          getChannel().newCall(getRegistryMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTRY = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PoliceOfficerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PoliceOfficerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REGISTRY:
          serviceImpl.registry((tech.claudioed.police.man.grpc.MessageData) request,
              (io.grpc.stub.StreamObserver<tech.claudioed.police.man.grpc.RegistryID>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class PoliceOfficerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PoliceOfficerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return tech.claudioed.police.man.grpc.PolicemanProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PoliceOfficer");
    }
  }

  private static final class PoliceOfficerFileDescriptorSupplier
      extends PoliceOfficerBaseDescriptorSupplier {
    PoliceOfficerFileDescriptorSupplier() {}
  }

  private static final class PoliceOfficerMethodDescriptorSupplier
      extends PoliceOfficerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PoliceOfficerMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PoliceOfficerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PoliceOfficerFileDescriptorSupplier())
              .addMethod(getRegistryMethod())
              .build();
        }
      }
    }
    return result;
  }
}
